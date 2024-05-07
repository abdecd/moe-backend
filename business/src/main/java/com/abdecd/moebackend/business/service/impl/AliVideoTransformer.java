package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.util.SpringContextUtil;
import com.abdecd.moebackend.business.lib.AliImmManager;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformCbArgs;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;
import com.abdecd.moebackend.business.service.VideoTransformer;
import com.abdecd.moebackend.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AliVideoTransformer implements VideoTransformer {
    @Autowired
    private RedisTemplate<String, VideoTransformTask> redisTemplate;
    @Autowired
    private AliImmManager aliImmManager;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public void transform(VideoTransformTask task, int ttlSeconds) {
        // 访问视频转码服务
        var aliTaskId = aliImmManager.transformVideo(task.getOriginPath(), task.getTargetPath());
        System.out.println("aliTaskId: " + aliTaskId);
        final String taskId = task.getId();
        // 轮询转码结果
        int perAskTtl = ttlSeconds / 10;
        Runnable cb = new Runnable() {
            int count = 10;
            @Override
            public void run() {
                var result = aliImmManager.getTransformResult(aliTaskId);
                System.out.println("aliTaskId: " + aliTaskId + ", result: " + result);
                if (result.equals("Succeeded")) {
                    transformCb(taskId);
                    return;
                } else if (result.equals("Failed")) {
                    return;
                }
                count--;
                if (count > 0) scheduledExecutor.schedule(this, perAskTtl, TimeUnit.SECONDS);
            }
        };
        scheduledExecutor.schedule(cb, perAskTtl, TimeUnit.SECONDS);
    }

    public void transformCb(String taskId) {
        // 用回调的taskId找到对应的task对象，使用反射触发回调
        var task = redisTemplate.opsForValue().get(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + taskId);
        if (task == null) return;
        var strs = task.getCbBeanNameAndMethodName().split("\\.");
        var bean = SpringContextUtil.getBean(strs[0]);
        try {
            Method method = bean.getClass().getDeclaredMethod(strs[1], VideoTransformCbArgs.class);
            method.invoke(bean, new VideoTransformCbArgs(
                    taskId,
                    VideoTransformCbArgs.Type.VIDEO_TRANSFORM,
                    VideoTransformCbArgs.Status.SUCCESS)
            );
            // todo 删掉临时文件夹中的对应视频
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

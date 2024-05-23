package com.abdecd.moebackend.business.service.video;

import com.abdecd.moebackend.business.lib.AliImmManager;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AliVideoTransformer implements VideoTransformer {
    @Autowired
    private AliImmManager aliImmManager;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    public CompletableFuture<Void> transform(VideoTransformTask task, int ttlSeconds, String username) {
        // 访问视频转码服务
        var t1 = CompletableFuture.supplyAsync(
                () -> transform(username, task.getOriginPath(), task.getTargetPaths()[VideoTransformTask.TaskType.VIDEO_TRANSFORM_360P.NUM], "640x360", ttlSeconds).join(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        var t2 = CompletableFuture.supplyAsync(
                () -> transform(username, task.getOriginPath(), task.getTargetPaths()[VideoTransformTask.TaskType.VIDEO_TRANSFORM_720P.NUM], "1280x720", ttlSeconds).join(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        var t3 = CompletableFuture.supplyAsync(
                () -> transform(username, task.getOriginPath(), task.getTargetPaths()[VideoTransformTask.TaskType.VIDEO_TRANSFORM_1080P.NUM], "1920x1080", ttlSeconds).join(),
                Executors.newVirtualThreadPerTaskExecutor()
        );
        try {
            CompletableFuture.allOf(t1, t2, t3).join();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CompletableFuture.completedFuture(null);
    }

    public CompletableFuture<String> transform(
            String username,
            String originPath,
            String targetPath,
            String widthAndHeight,
            int ttlSeconds
    ) {
        // 访问视频转码服务
        var aliTaskId = aliImmManager.transformVideo(username, originPath, targetPath, widthAndHeight);
        log.info("aliTaskId: " + aliTaskId);

        var resultFuture = new CompletableFuture<String>();
        // 轮询转码结果
        int perAskTtl = ttlSeconds / 10;
        Runnable cb = new Runnable() {
            int count = 10;
            @Override
            public void run() {
                var result = aliImmManager.getTransformResult(aliTaskId);
                log.info("aliTaskId: " + aliTaskId + ", result: " + result);
                if (result.equals("Succeeded")) {
                    resultFuture.complete("Succeeded");
                    return;
                } else if (result.equals("Failed")) {
                    resultFuture.completeExceptionally(new RuntimeException("Failed"));
                    return;
                }
                count--;
                if (count > 0) {
                    scheduledExecutor.schedule(this, perAskTtl, TimeUnit.SECONDS);
                } else {
                    resultFuture.completeExceptionally(new RuntimeException("Failed"));
                }
            }
        };
        scheduledExecutor.schedule(cb, perAskTtl, TimeUnit.SECONDS);
        return resultFuture;
    }
}

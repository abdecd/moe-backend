package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.mapper.VideoMapper;
import com.abdecd.moebackend.business.lib.ResourceLinkHandler;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformCbArgs;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.business.service.VideoTransformer;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoTransformer videoTransformer;
    @Autowired
    private RedisTemplate<String, VideoTransformTask> redisTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ResourceLinkHandler resourceLinkHandler;
    @Autowired
    private FileService fileService;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private static final int TRANSFORM_TASK_TTL = 300;
    private static final int TRANSFORM_TASK_REDIS_TTL = 1300;

    @Override
    public long addVideo(AddVideoDTO addVideoDTO) {
        // todo 检查是否是该用户的 videogroup

        var originPath = resourceLinkHandler.getRawPathFromVideoLink(addVideoDTO.getLink());
        if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);

        var entity = addVideoDTO.toEntity();
        videoMapper.insert(entity);

        // 封面和链接处理
        var coverUrl = addVideoDTO.getCover();
        try {
            var cover = fileService.changeTmpFileToStatic(
                    coverUrl,
                    "/video/" + entity.getId(),
                    "cover" + coverUrl.substring(coverUrl.lastIndexOf('.'))
            );
            if (cover.isEmpty()) throw new Exception();
            addVideoDTO.setCover(cover);
        } catch (Exception e) {
            videoMapper.deleteById(entity.getId());
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);
        }
        createTransformTask(entity.getId(), originPath, "videoServiceImpl.addVideoCb");

        return entity.getId();
    }

    /**
     * 创建转码任务，并在超时后删除
     * @param videoId :
     * @param originPath 如 tmp/1/video.mp4
     * @param cbNameStr 如 videoServiceImpl.addVideoCb
     */
    private void createTransformTask(Long videoId, String originPath, String cbNameStr) {
        // 视频转码
        var task = new VideoTransformTask()
                .setId(UUID.randomUUID() + "")
                .setVideoId(videoId)
                .setOriginPath(originPath)
                .setTargetPath("video/" + videoId + "/720p.mp4")
                .setStatus(VideoTransformTask.Status.WAITING)
                .setCbBeanNameAndMethodName(cbNameStr);
        redisTemplate.opsForValue().set(
                RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + task.getId(),
                task,
                TRANSFORM_TASK_REDIS_TTL,
                TimeUnit.SECONDS
        );
        stringRedisTemplate.opsForValue().set(
                RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + task.getVideoId(),
                task.getId(),
                TRANSFORM_TASK_REDIS_TTL,
                TimeUnit.SECONDS
        );
        videoTransformer.transform(task, TRANSFORM_TASK_TTL + 10);
        // todo 用消息队列 超时去删数据库
        var taskId = task.getId();
        scheduledExecutor.schedule(() -> {
            var nowTask = redisTemplate.opsForValue().get(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + taskId);
            if (nowTask != null) {
                redisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + nowTask.getId());
                stringRedisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + nowTask.getVideoId());
                videoMapper.deleteById(nowTask.getVideoId());
            }
        }, TRANSFORM_TASK_TTL, TimeUnit.SECONDS);
    }

    public void addVideoCb(VideoTransformCbArgs cbArgs) {
        if (
                cbArgs.getType().equals(VideoTransformCbArgs.Type.VIDEO_TRANSFORM)
                && cbArgs.getStatus().equals(VideoTransformCbArgs.Status.SUCCESS)
        ) {
            var task = redisTemplate.opsForValue().get(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + cbArgs.getTaskId());
            if (task != null) {
                redisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + task.getId());
                stringRedisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + task.getVideoId());
                addVideoFinal(task);
            }
        }
    }

    public void addVideoFinal(VideoTransformTask task) {
        videoMapper.update(new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, task.getVideoId())
                .set(Video::getLink, resourceLinkHandler.getVideoLink(task.getTargetPath()))
                .set(Video::getStatus, Video.Status.ENABLE)
        );
    }

    @Override
    public boolean checkVideoPending(Long videoId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + videoId));
    }

    @Override
    public void updateVideo(UpdateVideoDTO updateVideoDTO) {
        // todo 检查是否是该用户的 videogroup

        if (updateVideoDTO.getLink() != null) {
            var originPath = resourceLinkHandler.getRawPathFromVideoLink(updateVideoDTO.getLink());
            if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
                throw new BaseException(MessageConstant.INVALID_FILE_PATH);
            createTransformTask(updateVideoDTO.getId(), originPath, "videoServiceImpl.updateVideoCb");
        }

        var coverUrl = updateVideoDTO.getCover();
        if (coverUrl != null) {
            try {
                var cover = fileService.changeTmpFileToStatic(
                        coverUrl,
                        "/video/" + updateVideoDTO.getId(),
                        "cover" + coverUrl.substring(coverUrl.lastIndexOf('.'))
                );
                if (cover.isEmpty()) throw new Exception();
                updateVideoDTO.setCover(cover);
            } catch (Exception e) {
                throw new BaseException(MessageConstant.INVALID_FILE_PATH);
            }
        }

        var entity = updateVideoDTO.toEntity();
        videoMapper.updateById(entity);
    }

    public void updateVideoCb(VideoTransformCbArgs cbArgs) {
        if (
                cbArgs.getType().equals(VideoTransformCbArgs.Type.VIDEO_TRANSFORM)
                        && cbArgs.getStatus().equals(VideoTransformCbArgs.Status.SUCCESS)
        ) {
            var task = redisTemplate.opsForValue().get(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + cbArgs.getTaskId());
            if (task != null) {
                redisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + task.getId());
                stringRedisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + task.getVideoId());
                updateVideoFinal(task);
            }
        }
    }

    public void updateVideoFinal(VideoTransformTask task) {
        videoMapper.update(new LambdaUpdateWrapper<Video>()
                .eq(Video::getId, task.getVideoId())
                .set(Video::getLink, resourceLinkHandler.getVideoLink(task.getTargetPath()))
                .set(Video::getStatus, Video.Status.ENABLE)
        );
    }

    @Override
    public void deleteVideo(Long videoId) {
        var obj = videoMapper.selectById(videoId);
        if (obj == null) return;
        // 检查是否正在转码
        if (obj.getStatus().equals(Video.Status.TRANSFORMING)) return;
        // todo 检查是不是拥有者

        fileService.deleteDirInSystem("/video/" + videoId);
        videoMapper.deleteById(videoId);
    }

    @Override
    public VideoVO getVideo(Long videoId) {
        var video = videoMapper.selectById(videoId);
        if (video == null) return null;
        var vo = new VideoVO();
        BeanUtils.copyProperties(video, vo);
        return vo;
    }
}

package com.abdecd.moebackend.business.service.impl;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.common.util.SpringContextUtil;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.entity.VideoSrc;
import com.abdecd.moebackend.business.dao.mapper.VideoMapper;
import com.abdecd.moebackend.business.dao.mapper.VideoSrcMapper;
import com.abdecd.moebackend.business.lib.ResourceLinkHandler;
import com.abdecd.moebackend.business.pojo.dto.video.AddVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.UpdateVideoDTO;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformCbArgs;
import com.abdecd.moebackend.business.pojo.dto.video.VideoTransformTask;
import com.abdecd.moebackend.business.pojo.vo.video.VideoSrcVO;
import com.abdecd.moebackend.business.pojo.vo.video.VideoVO;
import com.abdecd.moebackend.business.service.FileService;
import com.abdecd.moebackend.business.service.VideoService;
import com.abdecd.moebackend.business.service.VideoTransformer;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private VideoSrcMapper videoSrcMapper;
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
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;
    private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();

    private static final int TRANSFORM_TASK_TTL = 600;
    private static final int TRANSFORM_TASK_REDIS_TTL = 1300;

    // todo 由于 BangumiVideoGroupServiceBase 上传修改删除还没用到这个类，故没有清除相关目录缓存
    @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#addVideoDTO.videoGroupId")
    @Transactional
    @Override
    public long addVideo(AddVideoDTO addVideoDTO) {
        videoGroupServiceBase.checkUserHaveTheGroup(addVideoDTO.getVideoGroupId());

        var originPath = resourceLinkHandler.getRawPathFromVideoLink(addVideoDTO.getLink());
        if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);

        var entity = addVideoDTO.toEntity();
        videoMapper.insert(entity);

        // 封面处理
        var coverUrl = addVideoDTO.getCover();
        try {
            var cover = fileService.changeTmpFileToStatic(
                    coverUrl,
                    "/video/" + entity.getId(),
                    "cover" + coverUrl.substring(coverUrl.lastIndexOf('.'))
            );
            if (cover.isEmpty()) throw new Exception(); // will be caught
            videoMapper.updateById(entity.setCover(cover));
        } catch (Exception e) {
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);
        }
        // 链接处理
        createTransformTask(entity.getId(), originPath);

        return entity.getId();
    }

    @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#addVideoDTO.videoGroupId")
    @Transactional
    @Override
    public long addVideoWithCoverResolved(AddVideoDTO addVideoDTO) {
        videoGroupServiceBase.checkUserHaveTheGroup(addVideoDTO.getVideoGroupId());

        var originPath = resourceLinkHandler.getRawPathFromVideoLink(addVideoDTO.getLink());
        if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
            throw new BaseException(MessageConstant.INVALID_FILE_PATH);

        var entity = addVideoDTO.toEntity();
        videoMapper.insert(entity);

        // 链接处理
        createTransformTask(entity.getId(), originPath);

        return entity.getId();
    }

    /**
     * 创建转码任务，并在超时后删除
     * @param videoId :
     * @param originPath 如 tmp/1/video.mp4
     */
    private void createTransformTask(Long videoId, String originPath) {
        // 视频转码
        var task = new VideoTransformTask()
                .setId(UUID.randomUUID() + "")
                .setVideoId(videoId)
                .setOriginPath(originPath)
                .setTargetPaths(new String[]{"video/" + videoId + "/360p.mp4", "video/" + videoId + "/720p.mp4", "video/" + videoId + "/1080p.mp4"})
                .setStatus(new VideoTransformTask.Status[]{VideoTransformTask.Status.WAITING, VideoTransformTask.Status.WAITING, VideoTransformTask.Status.WAITING})
                .setCbBeanNameAndMethodName("videoServiceImpl.videoTransformCb");
        // 保存任务
        redisTemplate.opsForValue().set(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + task.getId(), task, TRANSFORM_TASK_REDIS_TTL, TimeUnit.SECONDS);
        stringRedisTemplate.opsForValue().set(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + task.getVideoId(), task.getId(), TRANSFORM_TASK_REDIS_TTL, TimeUnit.SECONDS);

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

    @SuppressWarnings("unused")
    public void videoTransformCb(VideoTransformCbArgs cbArgs) {
        var lock = redissonClient.getLock(RedisConstant.VIDEO_TRANSFORM_TASK_CB_LOCK + cbArgs.getTaskId());
        lock.lock();
        log.info("videoTransformCb:" + cbArgs);
        try {
            if (cbArgs.getStatus().equals(VideoTransformCbArgs.Status.SUCCESS)) {
                var task = redisTemplate.opsForValue().get(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + cbArgs.getTaskId());
                if (task != null) {
                    // 更改状态并保存
                    task.getStatus()[cbArgs.getType().NUM] = VideoTransformTask.Status.SUCCESS;
                    redisTemplate.opsForValue().set(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + cbArgs.getTaskId(), task);
                    // 检验状态并调用结束任务
                    for (var status : task.getStatus()) {
                        if (status == VideoTransformTask.Status.WAITING) return;
                    }
                    videoTransformCbWillFinish(task);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void videoTransformCbWillFinish(VideoTransformTask task) {
        redisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_PREFIX + task.getId());
        stringRedisTemplate.delete(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + task.getVideoId());
        var self = SpringContextUtil.getBean(VideoServiceImpl.class);
        self.videoTransformSave(task);
    }

    @CacheEvict(cacheNames = RedisConstant.VIDEO_VO, key = "#task.videoId")
    @Transactional
    public void videoTransformSave(VideoTransformTask task) {
        for (var taskType : task.getTaskTypes()) {
            if (videoSrcMapper.update(new LambdaUpdateWrapper<VideoSrc>()
                    .eq(VideoSrc::getVideoId, task.getVideoId())
                    .eq(VideoSrc::getSrcName, taskType.NAME)
                    .set(VideoSrc::getSrc, resourceLinkHandler.getVideoLink(task.getTargetPaths()[taskType.NUM]))) == 0
            ) {
                videoSrcMapper.insert(new VideoSrc()
                        .setVideoId(task.getVideoId())
                        .setSrcName(taskType.NAME)
                        .setSrc(resourceLinkHandler.getVideoLink(task.getTargetPaths()[taskType.NUM]))
                );
            }
        }
        videoMapper.updateById(new Video()
                .setId(task.getVideoId())
                .setStatus(Video.Status.ENABLE)
        );
    }

    @Override
    public boolean checkVideoPending(Long videoId) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(RedisConstant.VIDEO_TRANSFORM_TASK_VIDEO_ID + videoId));
    }

    @Caching(evict = {
            @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#updateVideoDTO.videoGroupId"),
            @CacheEvict(cacheNames = RedisConstant.VIDEO_VO, key = "#updateVideoDTO.id")
    })
    @Override
    public void updateVideo(UpdateVideoDTO updateVideoDTO) {
        videoGroupServiceBase.checkUserHaveTheGroup(updateVideoDTO.getVideoGroupId());

        if (updateVideoDTO.getLink() != null) {
            var originPath = resourceLinkHandler.getRawPathFromVideoLink(updateVideoDTO.getLink());
            if (!originPath.startsWith("tmp/user" + UserContext.getUserId() + "/"))
                throw new BaseException(MessageConstant.INVALID_FILE_PATH);
            if (Objects.equals(videoMapper.selectById(updateVideoDTO.getId()).getStatus(), Video.Status.TRANSFORMING))
                throw new BaseException(MessageConstant.VIDEO_TRANSFORMING);
            createTransformTask(updateVideoDTO.getId(), originPath);
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

    @Caching(evict = {
            @CacheEvict(cacheNames = RedisConstant.VIDEO_GROUP_CONTENTS_CACHE, key = "#root.target.getVideo(#videoId).getVideoGroupId()"),
            @CacheEvict(cacheNames = RedisConstant.VIDEO_VO, key = "#videoId")
    })
    @Override
    public void deleteVideo(Long videoId) {
        var obj = videoMapper.selectById(videoId);
        if (obj == null) return;
        // 检查是不是拥有者
        videoGroupServiceBase.checkUserHaveTheGroup(obj.getVideoGroupId());
        // 检查是否正在转码
        if (obj.getStatus().equals(Video.Status.TRANSFORMING)) throw new BaseException(MessageConstant.VIDEO_TRANSFORMING);

        fileService.deleteDirInSystem("/video/" + videoId);
        videoMapper.deleteById(videoId);
    }

    @Cacheable(cacheNames = RedisConstant.VIDEO_VO, key = "#videoId", unless = "#result == null")
    @Override
    public VideoVO getVideo(Long videoId) {
        var video = videoMapper.selectById(videoId);
        if (video == null) return null;
        // todo 在转码中的视频是否需要返回？
        var vo = new VideoVO();
        BeanUtils.copyProperties(video, vo);
        vo.setSrc(new ArrayList<>(
                videoSrcMapper.selectList(new LambdaQueryWrapper<VideoSrc>()
                .eq(VideoSrc::getVideoId, videoId)
                .select(VideoSrc::getSrcName, VideoSrc::getSrc))
                .stream().map(videoSrc -> new VideoSrcVO(videoSrc.getSrcName(), videoSrc.getSrc())).toList()
        ));
        return vo;
    }

    @Override
    @Cacheable(cacheNames = RedisConstant.VIDEO_LIST_CACHE,key = "#videoGroupId") // todo
    public ArrayList<Video> getVideoListByGid(Long videoGroupId) {
        return videoMapper.selectByGid(videoGroupId);
    }
}

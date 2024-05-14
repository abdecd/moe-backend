package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FavoriteService {
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;

    public void add(Long userId, Long videoGroupId) {
        // 如果不存在
        if (videoGroupServiceBase.getVideoGroupType(videoGroupId) == null)
            throw new BaseException(MessageConstant.INVALID_VIDEO_GROUP);
        // 如果超过最大收藏数
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, 0, RedisConstant.FAVORITES_SIZE);
        var count = list == null ? 0L : list.size();
        if (1 + count > RedisConstant.FAVORITES_SIZE)
            throw new BaseException(MessageConstant.FAVORITES_EXCEED_LIMIT);
        // 如果已经加过了
        if (list != null && list.contains(videoGroupId))
            throw new BaseException(MessageConstant.FAVORITES_EXIST);
        redisTemplate.opsForList().rightPushAll(RedisConstant.FAVORITES + userId, videoGroupId);
        // 添加到视频组收藏量
        redisTemplate.opsForSet().add(RedisConstant.VIDEO_GROUP_FAVORITES_SET + videoGroupId, UserContext.getUserId());
    }

    public void delete(Long userId, long[] videoGroupIds) {
        for (var videoGroupId : videoGroupIds) {
            redisTemplate.opsForList().remove(RedisConstant.FAVORITES + userId, 0, videoGroupId);
            // 移除视频组收藏量
            redisTemplate.opsForSet().remove(RedisConstant.VIDEO_GROUP_FAVORITES_SET + videoGroupId, UserContext.getUserId());
        }
    }

    public List<VideoGroupWithDataVO> get(Long userId) {
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, 0, RedisConstant.FAVORITES_SIZE);
        if (list == null) return new ArrayList<>();
        return list.stream().map(videoGroupId -> videoGroupServiceBase.getVideoGroupWithData(videoGroupId)).toList();
    }

    public boolean exists(Long userId, Long videoGroupId) {
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, 0, RedisConstant.FAVORITES_SIZE);
        if (list == null) return false;
        return list.contains(videoGroupId);
    }

    public Long getVideoGroupFavoriteCount(Long videoGroupId) {
        var count = redisTemplate.opsForSet().size(RedisConstant.VIDEO_GROUP_FAVORITES_SET + videoGroupId);
        if (count == null) return 0L;
        return count;
    }

    public boolean isUserFavorite(Long userId, Long videoGroupId) {
        return exists(userId, videoGroupId);
    }

    public void addOrDeleteLike(Long userId, Long videoGroupId, Byte status) {
        if (Objects.equals(status, StatusConstant.DISABLE)) {
            redisTemplate.opsForSet().remove(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId, userId);
        } else if (Objects.equals(status, StatusConstant.ENABLE)) {
            redisTemplate.opsForSet().add(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId, userId);
        }
    }

    public Long getVideoGroupLikeCount(Long videoGroupId) {
        var count = redisTemplate.opsForSet().size(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId);
        if (count == null) return 0L;
        return count;
    }

    public boolean isUserLike(Long userId, Long videoGroupId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId, userId));
    }
}

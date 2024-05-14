package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    }

    public void delete(Long userId, long[] videoGroupIds) {
        for (var videoGroupId : videoGroupIds)
            redisTemplate.opsForList().remove(RedisConstant.FAVORITES + userId, 0, videoGroupId);
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
}

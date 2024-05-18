package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.common.exception.BaseException;
import com.abdecd.moebackend.business.dao.entity.VideoGroup;
import com.abdecd.moebackend.business.pojo.vo.favorite.BangumiVideoGroupFavoriteVO;
import com.abdecd.moebackend.business.pojo.vo.favorite.FavoriteVO;
import com.abdecd.moebackend.business.pojo.vo.videogroup.VideoGroupWithDataVO;
import com.abdecd.moebackend.business.service.plainuser.PlainUserHistoryService;
import com.abdecd.moebackend.business.service.videogroup.BangumiVideoGroupServiceBase;
import com.abdecd.moebackend.business.service.videogroup.VideoGroupServiceBase;
import com.abdecd.moebackend.common.constant.MessageConstant;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.abdecd.moebackend.common.result.PageVO;
import com.abdecd.tokenlogin.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class FavoriteService {
    @Autowired
    private RedisTemplate<String, Long> redisTemplate;
    @Autowired
    private VideoGroupServiceBase videoGroupServiceBase;
    @Autowired
    private PlainUserHistoryService plainUserHistoryService;
    @Autowired
    private BangumiVideoGroupServiceBase bangumiVideoGroupServiceBase;

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
            redisTemplate.opsForSet().remove(RedisConstant.VIDEO_GROUP_FAVORITES_SET + videoGroupId, userId);
        }
    }

    public PageVO<VideoGroupWithDataVO> get(Long userId, Integer page, Integer pageSize) {
        var total = redisTemplate.opsForList().size(RedisConstant.FAVORITES + userId);
        if (total == null) return new PageVO<>();
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, Math.max(0, (page - 1) * pageSize), Math.min((page * pageSize), RedisConstant.FAVORITES_SIZE));
        if (list == null) list = new ArrayList<>();
        var arr = list.stream().map(videoGroupId -> videoGroupServiceBase.getVideoGroupWithData(videoGroupId)).toList();
        return new PageVO<>(Math.toIntExact(total), arr);
    }

    public PageVO<FavoriteVO> getPlainFavorite(Long userId, Integer page, Integer pageSize) {
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, 0, RedisConstant.FAVORITES_SIZE);
        if (list == null) list = new ArrayList<>();
        var total = list.stream().filter(
                videoGroupId -> Objects.equals(videoGroupServiceBase.getVideoGroupType(videoGroupId), VideoGroup.Type.PLAIN_VIDEO_GROUP)
        ).count();
        var arr = list.stream().filter(
                videoGroupId -> Objects.equals(videoGroupServiceBase.getVideoGroupType(videoGroupId), VideoGroup.Type.PLAIN_VIDEO_GROUP)
        )
        .skip(Math.max(0, (page - 1) * pageSize))
        .limit(Math.min((page * pageSize), RedisConstant.FAVORITES_SIZE))
        .map(videoGroupId -> new FavoriteVO().setVideoGroupVO(videoGroupServiceBase.getVideoGroupInfo(videoGroupId))).toList();
        return new PageVO<>(Math.toIntExact(total), arr);
    }

    public PageVO<FavoriteVO> getBangumiFavorite(Long userId, Integer page, Integer pageSize) {
        var list = redisTemplate.opsForList().range(RedisConstant.FAVORITES + userId, 0, RedisConstant.FAVORITES_SIZE);
        if (list == null) list = new ArrayList<>();
        var total = list.stream().filter(
                videoGroupId -> Objects.equals(videoGroupServiceBase.getVideoGroupType(videoGroupId), VideoGroup.Type.ANIME_VIDEO_GROUP)
        ).count();
        var arr = list.stream().filter(
                videoGroupId -> Objects.equals(videoGroupServiceBase.getVideoGroupType(videoGroupId), VideoGroup.Type.ANIME_VIDEO_GROUP)
        )
        .skip(Math.max(0, (page - 1) * pageSize))
        .limit(Math.min((page * pageSize), RedisConstant.FAVORITES_SIZE))
        .map(this::formBangumiFavorite)
        .toList();
        return new PageVO<>(Math.toIntExact(total), arr);
    }

    public FavoriteVO formBangumiFavorite(Long videoGroupId) {
        var info = videoGroupServiceBase.getVideoGroupInfo(videoGroupId);
        var contents = bangumiVideoGroupServiceBase.getContents(videoGroupId);
        var latestVideoTitle = contents.isEmpty() ? null : contents.getFirst().getTitle();
        var lastWatch = plainUserHistoryService.getLatestHistory(videoGroupId);
        var lastWatchVideoTitle = lastWatch == null ? null : lastWatch.getVideoTitle();
        var lastWatchVideoId = lastWatch == null ? null : lastWatch.getVideoId();
        var lastWatchVideoIndex = lastWatch == null ? null : lastWatch.getVideoIndex();
        var vo = new BangumiVideoGroupFavoriteVO();
        vo.setVideoGroupVO(info);
        vo.setLatestVideoTitle(latestVideoTitle);
        vo.setLastWatchVideoTitle(lastWatchVideoTitle);
        vo.setLastWatchVideoId(lastWatchVideoId);
        vo.setLastWatchVideoIndex(lastWatchVideoIndex);
        return vo;
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
            var effected = redisTemplate.opsForSet().remove(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId, userId);
            if (effected == null || effected == 0)
                throw new BaseException(MessageConstant.LIKE_NOT_EXIST);
        } else if (Objects.equals(status, StatusConstant.ENABLE)) {
            var effected = redisTemplate.opsForSet().add(RedisConstant.VIDEO_GROUP_LIKE_SET + videoGroupId, userId);
            if (effected == null || effected == 0)
                throw new BaseException(MessageConstant.LIKE_EXIST);
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

package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.dao.entity.PlainUserHistory;
import com.abdecd.moebackend.business.pojo.dto.plainuser.AddHistoryDTO;
import com.abdecd.moebackend.business.pojo.vo.plainuser.HistoryVO;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.moebackend.common.result.PageVO;
import com.abdecd.tokenlogin.common.context.UserContext;
import jakarta.annotation.Nullable;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlainUserHistoryService {
    @Autowired
    private RedisTemplate<String, PlainUserHistory> redisTemplate;
    @Autowired
    private PlainUserService plainUserService;
    @Autowired
    private VideoService videoService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private PlainUserHistoryBatchSaver plainUserHistoryBatchSaver;

    public PageVO<HistoryVO> getHistory(Integer page, Integer pageSize) {
        var list = redisTemplate.opsForList().range(
                RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(),
                (long) (page - 1) * pageSize,
                (long) page * pageSize - 1
        );
        if (list == null) return new PageVO<>();
        var total = redisTemplate.opsForList().size(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId());
        if (total == null) return new PageVO<>();
        return new PageVO<>(Math.toIntExact(total), list.stream().map(this::formHistoryVO).toList());
    }

    public HistoryVO formHistoryVO(PlainUserHistory plainUserHistory) {
        return null;
//        var uploader = plainUserService.getPlainUserDetail(plainUserHistory.getUserId());
//        var video = videoService.getVideo(plainUserHistory.getVideoId());
//        var videoGroup = videoGroupService.getVideoGroup(plainUserHistory.getVideoGroupId());
//
//        var uploaderVO = new UploaderVO()
//                .setId(uploader.getUserId())
//                .setAvatar(uploader.getAvatar())
//                .setNickname(uploader.getNickname());
//        return new HistoryVO()
//                .setUploader(uploaderVO)
//                .setVideoGroupId(videoGroup.getVideoGroupId())
//                .setVideoGroupTitle(videoGroup.getVideoGroupTitle())
//                .setVideoGroupCover(videoGroup.getVideoGroupCover())
//                .setVideoId(video.getId())
//                .setVideoTitle(video.getTitle());
    }

    @Nullable
    public HistoryVO getLatestHistory(long videoGroupId) {
        var list = redisTemplate.opsForList().range(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), 0, -1);
        if (list == null) return null;
        for (var item : list) {
            if (item.getVideoGroupId().equals(videoGroupId)) {
                return formHistoryVO(item);
            }
        }
        return null;
    }

    public void addHistory(AddHistoryDTO addHistoryDTO) {
//        var video = videoService.getVideo(addHistoryDTO.getVideoId());
//        var videoGroup = videoGroupService.getVideoGroup(video.getVideoGroupId());// todo 返回多态基类 基类不缓存 对应的子类VO缓存
//        var entity = addHistoryDTO.toEntity(videoGroup.getUserId(), videoGroup.getId());
//
//        // add to redis
//        var lock = redissonClient.getLock(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId());
//        lock.lock();
//        try {
//            // delete old record
//            var list = redisTemplate.opsForList().range(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), 0, -1);
//            if (list == null) list = new ArrayList<>();
//            for (var item : list) {
//                if (item.getVideoId().equals(addHistoryDTO.getVideoId())) {
//                    redisTemplate.opsForList().remove(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), 0, item);
//                    break;
//                }
//            }
//            // add new record
//            redisTemplate.opsForList().leftPush(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), entity);
//            redisTemplate.opsForList().trim(
//                    RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(),
//                    0,
//                    RedisConstant.PLAIN_USER_HISTORY_SIZE
//            );
//        } finally {
//            lock.unlock();
//        }
//        // add to db
//        plainUserHistoryBatchSaver.add(entity);
    }

    public void deleteHistory(long[] videoGroupIds) {
//        // delete from redis
//        var lock = redissonClient.getLock(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId());
//        lock.lock();
//        try {
//            // delete record
//            var list = redisTemplate.opsForList().range(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), 0, -1);
//            if (list == null) list = new ArrayList<>();
//            for (var item : list) {
//                for (var id : videoGroupIds) {
//                    if (item.getVideoGroupId().equals(id)) {
//                        redisTemplate.opsForList().remove(RedisConstant.PLAIN_USER_HISTORY + UserContext.getUserId(), 0, item);
//                    }
//                }
//            }
//        } finally {
//            lock.unlock();
//        }
    }
}

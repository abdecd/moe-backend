package com.abdecd.moebackend.business.service.statistic;

import com.abdecd.moebackend.business.dao.entity.PlainUserTotalWatchTime;
import com.abdecd.moebackend.business.dao.mapper.PlainUserTotalWatchTimeMapper;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class TotalWatchTimeStatistic {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PlainUserTotalWatchTimeMapper plainUserTotalWatchTimeMapper;

    public void add(Long videoId, int addTime) {
        var oldT = get(videoId);
        stringRedisTemplate.opsForValue().set(
                RedisConstant.PLAIN_USER_TOTAL_WATCH_TIME + UserContext.getUserId() + ":" + videoId,
                oldT + addTime + ""
        );
    }

    @Nonnull
    public Long get(Long videoId) {
        var time = stringRedisTemplate.opsForValue().get(
                RedisConstant.PLAIN_USER_TOTAL_WATCH_TIME + UserContext.getUserId() + ":" + videoId
        );
        if (time == null) {
            var obj = plainUserTotalWatchTimeMapper.selectOne(new LambdaQueryWrapper<PlainUserTotalWatchTime>()
                    .eq(PlainUserTotalWatchTime::getUserId, UserContext.getUserId())
                    .eq(PlainUserTotalWatchTime::getVideoId, videoId)
                    .select(PlainUserTotalWatchTime::getTotalWatchTime)
            );
            time = Optional.ofNullable(obj)
                    .map(PlainUserTotalWatchTime::getTotalWatchTime)
                    .orElse(0L) + "";
        }
        stringRedisTemplate.opsForValue().set(
                RedisConstant.PLAIN_USER_TOTAL_WATCH_TIME + UserContext.getUserId() + ":" + videoId,
                time
        );
        return Long.parseLong(time);
    }

    public void saveAll() {
        var keys = new ArrayList<String>();
        ScanOptions options = ScanOptions.scanOptions()
                .match(RedisConstant.PLAIN_USER_TOTAL_WATCH_TIME + "*:*")
                .count(10)
                .build();
        try (var iter = stringRedisTemplate.scan(options)) {
            while (iter.hasNext()) {
                keys.add(iter.next());
            }
        }
        if (keys.isEmpty()) return;
        for (var key : keys) {
            var totalWatchTime = stringRedisTemplate.opsForValue().get(key);
            if (totalWatchTime == null) continue;

            // parse userId and videoId
            int index = key.lastIndexOf(":") - 1;
            for (; index >= 0; index--) if (key.charAt(index) == ':') break;
            if (index == -1) throw new RuntimeException("key format error");
            var keyy = key.substring(index + 1).split(":");
            var userId = Long.parseLong(keyy[0]);
            var videoId = Long.parseLong(keyy[1]);

            var cnt = plainUserTotalWatchTimeMapper.update(new LambdaUpdateWrapper<PlainUserTotalWatchTime>()
                    .eq(PlainUserTotalWatchTime::getUserId, userId)
                    .eq(PlainUserTotalWatchTime::getVideoId, videoId)
                    .set(PlainUserTotalWatchTime::getTotalWatchTime, Long.parseLong(totalWatchTime))
            );
            if (cnt == 0) {
                plainUserTotalWatchTimeMapper.insert(new PlainUserTotalWatchTime()
                        .setUserId(userId)
                        .setVideoId(videoId)
                        .setTotalWatchTime(Long.parseLong(totalWatchTime))
                );
            }
            stringRedisTemplate.delete(key);
        }
    }
}

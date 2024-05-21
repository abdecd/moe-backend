package com.abdecd.moebackend.business.service.statistic;

import com.abdecd.moebackend.business.dao.entity.PlainUserLastWatchTime;
import com.abdecd.moebackend.business.dao.mapper.PlainUserLastWatchTimeMapper;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class LastWatchTimeStatistic {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private PlainUserLastWatchTimeMapper plainUserLastWatchTimeMapper;

    public void add(Long videoId, String watchTime) {
        stringRedisTemplate.opsForValue().set(
                RedisConstant.PLAIN_USER_LAST_WATCH_TIME + UserContext.getUserId() + ":" + videoId,
                watchTime
        );
    }

    public Long get(Long videoId) {
        var time = stringRedisTemplate.opsForValue().get(
                RedisConstant.PLAIN_USER_LAST_WATCH_TIME + UserContext.getUserId() + ":" + videoId
        );
        if (time == null) {
            var obj = plainUserLastWatchTimeMapper.selectOne(new LambdaQueryWrapper<PlainUserLastWatchTime>()
                    .eq(PlainUserLastWatchTime::getUserId, UserContext.getUserId())
                    .eq(PlainUserLastWatchTime::getVideoId, videoId)
                    .select(PlainUserLastWatchTime::getLastWatchTime)
            );
            time = Optional.ofNullable(obj)
                    .map(PlainUserLastWatchTime::getLastWatchTime)
                    .orElse(0L) + "";
        }
        stringRedisTemplate.opsForValue().set(
                RedisConstant.PLAIN_USER_LAST_WATCH_TIME + UserContext.getUserId() + ":" + videoId,
                time
        );
        return Long.parseLong(time);
    }

    public void saveAll() {
        var keys = new ArrayList<String>();
        ScanOptions options = ScanOptions.scanOptions()
                .match(RedisConstant.PLAIN_USER_LAST_WATCH_TIME + "*:*")
                .count(10)
                .build();
        try (var iter = stringRedisTemplate.scan(options)) {
            while (iter.hasNext()) {
                keys.add(iter.next());
            }
        }
        if (keys.isEmpty()) return;
        for (var key : keys) {
            var lastWatchTime = stringRedisTemplate.opsForValue().get(key);
            if (lastWatchTime == null) continue;

            // parse userId and videoId
            int index = key.lastIndexOf(":") - 1;
            for (; index >= 0; index--) if (key.charAt(index) == ':') break;
            if (index == -1) throw new RuntimeException("key format error");
            var keyy = key.substring(index + 1).split(":");
            var userId = Long.parseLong(keyy[0]);
            var videoId = Long.parseLong(keyy[1]);

            var cnt = plainUserLastWatchTimeMapper.update(new LambdaUpdateWrapper<PlainUserLastWatchTime>()
                    .eq(PlainUserLastWatchTime::getUserId, userId)
                    .eq(PlainUserLastWatchTime::getVideoId, videoId)
                    .set(PlainUserLastWatchTime::getLastWatchTime, Long.parseLong(lastWatchTime))
            );
            if (cnt == 0) {
                plainUserLastWatchTimeMapper.insert(new PlainUserLastWatchTime()
                        .setUserId(userId)
                        .setVideoId(videoId)
                        .setLastWatchTime(Long.parseLong(lastWatchTime))
                );
            }
            stringRedisTemplate.delete(key);
        }
    }
}

package com.abdecd.moebackend.business.service;

import com.abdecd.moebackend.business.pojo.dto.statistic.VideoPlayDTO;
import com.abdecd.moebackend.business.service.statistic.LastWatchTimeStatistic;
import com.abdecd.moebackend.business.service.statistic.TotalWatchTimeStatistic;
import com.abdecd.moebackend.common.constant.RedisConstant;
import com.abdecd.tokenlogin.common.context.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class StatisticService {
    @Autowired
    private LastWatchTimeStatistic lastWatchTimeStatistic;
    @Autowired
    private TotalWatchTimeStatistic totalWatchTimeStatistic;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void cntVideoPlay(VideoPlayDTO videoPlayDTO, int addTime) {
        // 记录上次观看位置
        lastWatchTimeStatistic.add(videoPlayDTO.getVideoId(), videoPlayDTO.getWatchTime());
        // 记录播放总时长
        totalWatchTimeStatistic.add(videoPlayDTO.getVideoId(), addTime);
        // 统计播放量
        // todo 统计未登录用户
        stringRedisTemplate.opsForHyperLogLog().add(
                RedisConstant.STATISTIC_VIDEO_PLAY_CNT + videoPlayDTO.getVideoId(),
                UserContext.getUserId() + ""
        );
    }
}

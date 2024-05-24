package com.abdecd.moebackend.business.task;

import com.abdecd.moebackend.business.dao.entity.BangumiTimeTable;
import com.abdecd.moebackend.business.dao.entity.Video;
import com.abdecd.moebackend.business.dao.mapper.BangumiTimeTableMapper;
import com.abdecd.moebackend.business.service.video.VideoService;
import com.abdecd.moebackend.common.constant.StatusConstant;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BangumiTimeTableRefresher {
    @Autowired
    private BangumiTimeTableMapper bangumiTimeTableMapper;
    @Autowired
    private VideoService videoService;

    @Scheduled(cron = "0/30 * * * * ?")
    public void refresh() {
        var needUpdate = bangumiTimeTableMapper.selectList(new LambdaQueryWrapper<BangumiTimeTable>()
                .eq(BangumiTimeTable::getStatus, StatusConstant.ENABLE)
                .lt(BangumiTimeTable::getUpdateTime, LocalDateTime.now())
        );
        for (BangumiTimeTable bangumiTimeTable : needUpdate) {
            videoService.videoStatusUpdate(bangumiTimeTable.getVideoId(), Video.Status.ENABLE);
        }
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void clearOld() {
        bangumiTimeTableMapper.delete(new LambdaQueryWrapper<BangumiTimeTable>()
                .lt(BangumiTimeTable::getUpdateTime, LocalDateTime.now().minusDays(1))
        );
    }
}

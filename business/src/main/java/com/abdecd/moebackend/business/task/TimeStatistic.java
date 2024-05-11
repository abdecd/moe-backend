package com.abdecd.moebackend.business.task;

import com.abdecd.moebackend.business.service.statistic.LastWatchTimeStatistic;
import com.abdecd.moebackend.business.service.statistic.TotalWatchTimeStatistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean({LastWatchTimeStatistic.class, TotalWatchTimeStatistic.class})
public class TimeStatistic {
    @Autowired
    private LastWatchTimeStatistic lastWatchTimeStatistic;
    @Autowired
    private TotalWatchTimeStatistic totalWatchTimeStatistic;

    @Scheduled(cron = "0 0 4 * * ?")
    public void save() {
        lastWatchTimeStatistic.saveAll();
        totalWatchTimeStatistic.saveAll();
    }
}

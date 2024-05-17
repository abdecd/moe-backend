package com.abdecd.moebackend.business.task;

import com.abdecd.moebackend.business.service.plainuser.PlainUserHistoryBatchSaver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnBean(PlainUserHistoryBatchSaver.class)
public class PlainUserHistorySave {
    @Autowired
    PlainUserHistoryBatchSaver batchSaver;

    @Scheduled(cron = "0/5 * * * * ?")
    public void save() {
        batchSaver.save();
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void clear() {
        batchSaver.clearOldHistory();
    }
}

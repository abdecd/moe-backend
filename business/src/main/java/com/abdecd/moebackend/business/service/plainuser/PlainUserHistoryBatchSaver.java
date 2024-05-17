package com.abdecd.moebackend.business.service.plainuser;

import com.abdecd.moebackend.business.dao.entity.PlainUserHistory;
import com.abdecd.moebackend.business.lib.BaseBatchSaver;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PlainUserHistoryBatchSaver extends BaseBatchSaver<PlainUserHistory> {
    private static final long CLEAR_SIZE = 1000000;

    /**
     * 数据量过大时删除3个月前的数据
     */
    public void clearOldHistory() {
        if (Db.count(PlainUserHistory.class) < CLEAR_SIZE) return;
        Db.lambdaUpdate(PlainUserHistory.class)
                .lt(PlainUserHistory::getTimestamp, LocalDateTime.now().minusMonths(3))
                .remove();
    }
}

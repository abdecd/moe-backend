package com.abdecd.moebackend.business.lib;

import com.baomidou.mybatisplus.extension.toolkit.Db;

import java.util.ArrayList;
import java.util.List;

public class BaseBatchSaver<T> {
    protected int SAVE_SIZE = 1000;
    private final List<T> objList = new ArrayList<>(SAVE_SIZE);

    synchronized public void add(T obj) {
        objList.add(obj);
        if (objList.size() >= SAVE_SIZE) {
            save();
        }
    }

    /**
     * 记得设置定时任务
     */
    synchronized public void save() {
        var tmpList = new ArrayList<>(objList);
        Db.saveBatch(tmpList);
        objList.clear();
    }
}

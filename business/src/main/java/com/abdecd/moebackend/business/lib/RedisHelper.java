package com.abdecd.moebackend.business.lib;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisHelper {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public List<String> scan(String pattern) {
        var keys = new ArrayList<String>();
        ScanOptions options = ScanOptions.scanOptions()
                .match(pattern)
                .count(10)
                .build();
        try (var iter = stringRedisTemplate.scan(options)) {
            while (iter.hasNext()) {
                keys.add(iter.next());
            }
        }
        return keys;
    }
}

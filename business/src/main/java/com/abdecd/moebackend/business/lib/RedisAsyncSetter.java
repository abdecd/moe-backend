package com.abdecd.moebackend.business.lib;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

@Service
public class RedisAsyncSetter<T> {
    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private RedissonClient redissonClient;

    public void set(RedisTemplate<String, T> redisTemplate, String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public T get(RedisTemplate<String, T> redisTemplate, String key, Supplier<T> failCb) {
        var value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            var lock = redissonClient.getLock(key + ":lock");
            lock.lock();
            try {
                var tmp = redisTemplate.opsForValue().get(key);
                if (tmp != null) return tmp;
                value = failCb.get();
                redisTemplate.opsForValue().set(key, value);
            } finally {
                lock.unlock();
            }
        }
        return value;
    }

    /**
     * @param redisTemplate :
     * @param pattern :
     * @param keyStrExcludeLength 返回的keys中key不需要的前缀的长度，作用是会让key去掉前面若干个字符
     * @param deleteAllKeys 是否删除所有刚刚调用该方法时返回过的键
     */
    public void handleAll(
            RedisTemplate<String, T> redisTemplate,
            String pattern,
            int keyStrExcludeLength,
            BiConsumer<List<String>, List<T>> cb,
            boolean deleteAllKeys
    ) {
        var keys = redisHelper.scan(pattern);
        if (keys.isEmpty()) return;
        var values = redisTemplate.opsForValue().multiGet(keys);
        cb.accept(keys.stream().map(key -> key.substring(keyStrExcludeLength)).toList(), values);
        if (deleteAllKeys) redisTemplate.delete(keys);
    }
}

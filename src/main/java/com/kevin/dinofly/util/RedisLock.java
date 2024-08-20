package com.kevin.dinofly.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean tryLock(String key, String value, long expireTime) {
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(key, value, expireTime, TimeUnit.MILLISECONDS);
        return Boolean.TRUE.equals(acquired);
    }

    public void unlock(String key, String value) {
        String currentValue = redisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            redisTemplate.delete(key);
        }
    }
}

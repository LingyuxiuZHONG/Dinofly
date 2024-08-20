package com.kevin.dinofly.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;


@Component
public class IdGenerator {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //设置起始时间，我这里设定的是2022.01.01 00:00:00
    public static final Long BEGIN_TIMESTAMP = LocalDateTime.of(2024,1,1,0,0,0).toEpochSecond(ZoneOffset.UTC);
    //序列号长度
    public static final Long COUNT_BIT = 32L;

    public long nextId(String keyPrefix){
        // 1. 生成时间戳
        LocalDateTime now = LocalDateTime.now();
        long currentSecond = now.toEpochSecond(ZoneOffset.UTC);
        long timestamp = currentSecond - BEGIN_TIMESTAMP;

        // 2. 生成序列号
        String date = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        long count = stringRedisTemplate.opsForValue().increment("dinofly:inc:" + keyPrefix + ":" + date);

        // 3. 拼接并返回，使用位运算
        return timestamp << COUNT_BIT | count;
    }
}

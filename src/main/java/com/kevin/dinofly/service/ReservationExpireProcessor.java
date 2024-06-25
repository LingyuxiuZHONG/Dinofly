package com.kevin.dinofly.service;

import com.kevin.dinofly.mapper.ReservationMapper;
import com.kevin.dinofly.util.Constants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class ReservationExpireProcessor {

    private static final String STREAM_KEY = Constants.RESERVATION_EXPIRE_STREAM_KEY;
    private static final String GROUP_NAME = Constants.RESERVATION_GROUP_NAME;

    private static final String CONSUMER_NAME = Constants.RESERVATION_CONSUMER_NAME;

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostConstruct
    public void init() {
        try {
            stringRedisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP_NAME);
        } catch (Exception e) {
            // 忽略已存在的消费组错误
        }
    }


    @Scheduled(fixedRate = 10000)
    public void processSingleMessage() {
        List<MapRecord<String, Object, Object>> messages = stringRedisTemplate.opsForStream().read(
                Consumer.from(GROUP_NAME, CONSUMER_NAME),
                StreamReadOptions.empty().count(1),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
        );

        if (messages == null || messages.isEmpty()) {
            return;
        }


        MapRecord<String, Object, Object> message = messages.get(0);
        String reservationId = (String) message.getValue().get("reservationId");
        log.info("Processing reservationId: {}", reservationId);

        // 处理预订关闭逻辑
        reservationMapper.updateReservationStatus(reservationId, "TRADE_CLOSED");
        stringRedisTemplate.opsForStream().acknowledge(STREAM_KEY,GROUP_NAME, message.getId());
    }

}

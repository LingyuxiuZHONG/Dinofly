package com.kevin.dinofly.service.listener;

import com.kevin.dinofly.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private static final String RESERVATION_TIMEOUT_KEY_PREFIX = Constants.RESERVATION_TIMEOUT_KEY_PREFIX;
    private static final String RESERVATION_EXPIRE_STREAM_KEY = Constants.RESERVATION_EXPIRE_STREAM_KEY;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        if (expiredKey.startsWith(RESERVATION_TIMEOUT_KEY_PREFIX)) {
            String reservationId = expiredKey.substring(RESERVATION_TIMEOUT_KEY_PREFIX.length());
            stringRedisTemplate.opsForStream().add(RESERVATION_EXPIRE_STREAM_KEY, Map.of("reservationId",reservationId,"status","TRADE_CLOSED"));
        }
    }
}

package com.kevin.dinofly.service.message;


import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class AbstractMessageProcessorService {
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    protected String streamKey;

    protected String groupName;

    protected String consumerName;

    @PostConstruct
    public void init() {
        try {
            // XGROUP CREATE <key> <groupname> <id> [MKSTREAM]
            // <id>：开始消费的 ID。通常使用 0 或 $，其中 0 表示从流的开头开始消费，$ 表示从流的最新条目开始消费。
            stringRedisTemplate.opsForStream().createGroup(streamKey, groupName);
        } catch (Exception e) {
            // 忽略已存在的消费组错误
        }
    }

    @Scheduled(fixedRate = 10000)
    public void processSingleMessage() {
        // XREADGROUP GROUP <groupname> <consumername> COUNT <count> BLOCK <milliseconds> STREAMS <key> <id>
        List<MapRecord<String, Object, Object>> messages = stringRedisTemplate.opsForStream().read(
                Consumer.from(groupName, consumerName),
                StreamReadOptions.empty().count(1),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed())
        );

        if (messages == null || messages.isEmpty()) {
            return;
        }

        MapRecord<String, Object, Object> message = messages.get(0);
        Map<String, Object> messageBody = new HashMap<>();
        message.getValue().forEach((k, v) -> messageBody.put((String) k, v));

        log.info("Processing message: {}", messageBody);
        processMessageBody(messageBody);
        stringRedisTemplate.opsForStream().acknowledge(streamKey, groupName, message.getId());
    }

    protected abstract void processMessageBody(Map<String, Object> message);


}

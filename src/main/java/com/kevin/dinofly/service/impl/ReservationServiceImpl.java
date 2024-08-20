package com.kevin.dinofly.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.kevin.dinofly.mapper.ReservationMapper;
import com.kevin.dinofly.model.Reservation;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.ReservationService;
import com.kevin.dinofly.util.Constants;
import com.kevin.dinofly.util.IdGenerator;
import com.kevin.dinofly.util.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private IdGenerator idGenerator;

    @Autowired
    private RedisLock redisLock;

    private static final String ID_PREFIX = "reservation";

    private static final String RESERVATION_TIMEOUT_KEY_PREFIX = Constants.RESERVATION_TIMEOUT_KEY_PREFIX;

    private static final long RESERVATION_TIMEOUT = 15 * 60;// 15min

    private static final long LOCK_EXPIRE_TIME = 30 * 1000;// 30s


    private static final String RESERVATION_EXPIRE_STREAM_KEY = Constants.RESERVATION_EXPIRE_STREAM_KEY;




    @Override
    public ResponseEntity<?> createReservation(Long adId, Reservation reservation, CustomUserDetails userDetails) {
        String lockKey = "lock:ad:" + adId;
        String lockValue = UUID.randomUUID().toString();
        try{
            //  1. 获取分布式锁，防止并发预订同一房源
            boolean acquired = redisLock.tryLock(lockKey,lockValue,LOCK_EXPIRE_TIME);
            if(!acquired){
                log.warn("用户 {} 尝试获取房源 {} 的锁失败", userDetails.getId(), adId);
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("房源正在被其他用户预订，请稍后再试。");
            }

            // 2. 检查指定时间段内房源是否已经被预订
            boolean isOverlapping = reservationMapper.isOverlappingReservationExists(adId, reservation.getStartDate(), reservation.getEndDate());
            if (isOverlapping) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("所选时间段已被预订，请选择其他时间段。");
            }

            // 3. 创建新的预订记录
            long id = idGenerator.nextId(ID_PREFIX);
            reservation.setReservationId(id);
            reservation.setUserId(userDetails.getId());
            reservation.setAdId(adId);
            reservation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            reservation.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            reservation.setStatus("WAIT_BUYER_PAY");
            reservationMapper.createReservation(reservation);

            // 4. 设置Redis键，用于处理超时订单
            stringRedisTemplate.opsForValue().set(RESERVATION_TIMEOUT_KEY_PREFIX + id, "WAIT_BUYER_PAY",RESERVATION_TIMEOUT, TimeUnit.SECONDS);


        }finally {
            redisLock.unlock(lockKey,lockValue);
        }


        return ResponseEntity.ok(reservation);


    }


    @Transactional
    @Override
    public ResponseEntity<?> processPayment(String reservationId) {
        // 1. 模拟支付逻辑
        boolean paymentSuccess = simulatePayment();

        // 2. 判断支付结果
        if (paymentSuccess) {
            // 2.1 支付成功，删除Redis中的超时键
            stringRedisTemplate.delete(RESERVATION_TIMEOUT_KEY_PREFIX + reservationId);

            // 2.2 将更新预订状态为 "TRADE_SUCCESS"的任务放入消息队列
            stringRedisTemplate.opsForStream().add(RESERVATION_EXPIRE_STREAM_KEY, Map.of("reservationId",reservationId,"status","TRADE_SUCCESS"));
            return ResponseEntity.ok("支付成功，预订已确认。");
        } else {
            // 3. 支付失败，提示用户继续支付并返回剩余支付时间
            Long remainingTime = stringRedisTemplate.getExpire(RESERVATION_TIMEOUT_KEY_PREFIX + reservationId, TimeUnit.MINUTES);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("支付失败，请稍后重试。请尽快支付，订单在" + remainingTime + "分钟后将被关闭");
        }
    }

    @Override
    public ResponseEntity<?> getReservationById(String reservationId) {
//          // 尝试从Redis中获取预订信息
//        String reservationJson = stringRedisTemplate.opsForValue().get(RESERVATION_KEY_PREFIX + reservationId);
//        if (reservationJson != null) {
//            return JSONUtil.toBean(reservationJson, Reservation.class);
//        }
//        // Redis中没有预订信息，从数据库获取
//        Reservation reservation = reservationMapper.getReservationById(reservationId);
//        if (reservation != null) {
//            // 将获取到的预订信息存入Redis
//            stringRedisTemplate.opsForValue().set(RESERVATION_KEY_PREFIX + reservationId, JSONUtil.toJsonStr(reservation));
//        }
        Reservation reservation = reservationMapper.getReservationById(reservationId);
        if(ObjectUtil.isEmpty(reservation)){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(reservation);
        }
    }

    @Override
    public void updateReservationStatus(String reservationId, String status) {
        reservationMapper.updateReservationStatus(reservationId,status);
    }



    private boolean simulatePayment() {
        return new Random().nextBoolean();
    }




}

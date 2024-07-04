package com.kevin.dinofly.service.impl;

import cn.hutool.json.JSONUtil;
import com.kevin.dinofly.mapper.ReservationMapper;
import com.kevin.dinofly.model.Reservation;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.ReservationService;
import com.kevin.dinofly.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
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

    private static final String RESERVATION_KEY_PREFIX = Constants.RESERVATION_KEY_PREFIX ;

    private static final int RESERVATION_AUTO_CANCEL_TIME = Constants.RESERVATION_AUTO_CANCEL_TIME;





    @Override
    public Reservation createReservation(Long adId, Reservation reservation, CustomUserDetails userDetails) {

            String id = UUID.randomUUID().toString();
            reservation.setReservationId(id);
            reservation.setUserId(userDetails.getId());
            reservation.setAdId(adId);
            reservation.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            reservation.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            reservation.setStatus("WAIT_BUYER_PAY");

            // 将订单状态缓存到 Redis
            stringRedisTemplate.opsForValue().set(RESERVATION_KEY_PREFIX + id, JSONUtil.toJsonStr(reservation),RESERVATION_AUTO_CANCEL_TIME, TimeUnit.SECONDS);

            // 异步保存预订信息
            executorService.submit(() -> {
                try {
                    reservationMapper.createReservation(reservation);
                    log.info("Reservation saved successfully: {}", reservation.getReservationId());
                } catch (Exception e) {
                    log.error("Error saving reservation: {}", e.getMessage(), e);
                    stringRedisTemplate.delete(RESERVATION_KEY_PREFIX + id);

                }
            });
            return reservation;


    }


    @Transactional
    @Override
    public String processPayment(String reservationId) {
        boolean paymentSuccess = simulatePayment();

        if (paymentSuccess) {
            // 更新预订状态
            executorService.submit(() -> {
                updateReservationStatus(reservationId,"TRADE_SUCCESS");
                stringRedisTemplate.delete(RESERVATION_KEY_PREFIX + reservationId);
            });
            return "Success";
        } else {
            return "Payment failed, please pay as soon as possible. The order will be canceled after " + stringRedisTemplate.getExpire(RESERVATION_KEY_PREFIX + reservationId,TimeUnit.MINUTES);
        }
    }

    @Override
    public Reservation getReservationById(String reservationId) {
        // 尝试从Redis中获取预订信息
        String reservationJson = stringRedisTemplate.opsForValue().get(RESERVATION_KEY_PREFIX + reservationId);
        if (reservationJson != null) {
            return JSONUtil.toBean(reservationJson, Reservation.class);
        }

        // Redis中没有预订信息，从数据库获取
        Reservation reservation = reservationMapper.getReservationById(reservationId);
        if (reservation != null) {
            // 将获取到的预订信息存入Redis
            stringRedisTemplate.opsForValue().set(RESERVATION_KEY_PREFIX + reservationId, JSONUtil.toJsonStr(reservation));
        }
        return reservation;
    }

    @Override
    public void updateReservationStatus(String reservationId, String status) {
        reservationMapper.updateReservationStatus(reservationId,status);
    }



    private boolean simulatePayment() {
        // 模拟支付逻辑
        Random r = new Random();

        return r.nextBoolean(); // 假设支付成功
    }


}

package com.kevin.dinofly.controller;

import com.kevin.dinofly.model.Reservation;
import com.kevin.dinofly.model.dto.PaymentRequest;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/{adId}")
    public ResponseEntity<?> createReservation(@PathVariable Long adId, @RequestBody Reservation reservation, @AuthenticationPrincipal CustomUserDetails userDetails){

        Reservation afterCreate = reservationService.createReservation(adId,reservation,userDetails);
        return ResponseEntity.ok(afterCreate);

    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitPayment(@RequestBody PaymentRequest paymentRequest) {
        String paymentStatus = reservationService.processPayment(paymentRequest.getReservationId());
        return ResponseEntity.ok(paymentStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable String id) {
        try {
            // 调用服务层获取预订信息
            Reservation reservation = reservationService.getReservationById(id);
            if (reservation != null) {
                return ResponseEntity.ok(reservation);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // 处理异常情况
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve reservation");
        }
    }
}

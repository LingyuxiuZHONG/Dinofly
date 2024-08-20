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

        return reservationService.createReservation(adId,reservation,userDetails);

    }

    @PostMapping("/submit")
    public ResponseEntity<?> submitPayment(@RequestBody PaymentRequest paymentRequest) {
        return reservationService.processPayment(paymentRequest.getReservationId());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable String id) {
        return reservationService.getReservationById(id);

    }
}

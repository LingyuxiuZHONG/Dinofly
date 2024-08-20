package com.kevin.dinofly.service;

import com.kevin.dinofly.model.Reservation;
import com.kevin.dinofly.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;

public interface ReservationService {
    ResponseEntity<?> createReservation(Long adId, Reservation reservation, CustomUserDetails userDetails);

    ResponseEntity<?> processPayment(String reservationId);

    ResponseEntity<?> getReservationById(String id);


    void updateReservationStatus(String reservationId, String status);
}

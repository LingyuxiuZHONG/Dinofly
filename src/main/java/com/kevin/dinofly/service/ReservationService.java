package com.kevin.dinofly.service;

import com.kevin.dinofly.model.Reservation;
import com.kevin.dinofly.security.CustomUserDetails;

public interface ReservationService {
    Reservation createReservation(Long adId, Reservation reservation, CustomUserDetails userDetails);

    String processPayment(String reservationId);

    Reservation getReservationById(String id);


    void updateReservationStatus(String reservationId, String status);
}

package com.kevin.dinofly.service;

import com.kevin.dinofly.model.Reservation;

public interface ReservationService {
    Reservation createReservation(Long adId, Reservation reservation);

    String processPayment(String reservationId);

    Reservation getReservationById(String id);


    void updateReservationStatus(String reservationId, String status);
}

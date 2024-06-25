package com.kevin.dinofly.model;


import lombok.Data;

import java.util.Date;

@Data
public class Reservation {
    private String reservationId;
    private long userId;
    private long adId;
    private Date startDate;
    private Date endDate;
    private double price;
    private double total;
    private Date createdAt;
    private Date updatedAt;
    private String status;
    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + reservationId +
                ", userId=" + userId +
                ", adId=" + adId +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", price=" + price +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status='" + status + '\'' +
                '}';
    }
}

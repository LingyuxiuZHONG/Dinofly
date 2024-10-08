package com.kevin.dinofly.model;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Ad {
    private Long adId;
    private String homeType;
    private Integer totalOccupancy;
    private Integer totalBedrooms;
    private Integer totalBathrooms;
    private String description;
    private String address;
    private Boolean hasTv;
    private Boolean hasKitchen;
    private Boolean hasAirCon;
    private Boolean hasHeating;
    private Boolean hasInternet;
    private Integer price;
    private Long ownerId;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double latitude;
    private Double longitude;

}

package com.kevin.dinofly.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class AdDTO {
    private Long id;
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
//    private LocalDateTime publishedAt;
//    private Integer ownerId;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
//    private Double latitude;
//    private Double longitude;
}

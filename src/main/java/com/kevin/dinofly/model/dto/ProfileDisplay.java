package com.kevin.dinofly.model.dto;


import lombok.Data;

import java.sql.Date;

@Data
public class ProfileDisplay {
    private Long userId;
    private String username;
    private String email;
    private String phoneNumber;
    private String description;
    private String profileImage;
    private String sex;
    private Date createAt;
}

package com.kevin.dinofly.model.dto;

import lombok.Data;


@Data
public class ProfileUpdate {
    private String fieldUpdate;
    private Long userId;
    private String username;

    private String password;
    private String currentPassword;

    private String email;

    private String phoneNumber;
    private String code;

    private String description;
    private String sex;
}

package com.kevin.dinofly.model;


import lombok.Data;


import java.sql.Date;
import java.sql.Timestamp;

@Data
public class User {
    private Long userId;
    private String username;
    private String password;
    private String email;
    private Timestamp emailVerifiedAt;
    private String phoneNumber;
    private String description;
    private String profileImage = "default.jpg";
    private String role;
    private String sex;
    private Date createdAt;

}

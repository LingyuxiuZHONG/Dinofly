package com.kevin.dinofly.model;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.sql.Timestamp;

@Data
public class User {
    private Long id;
    private String username;
    private String password;

    private String email;
    private Timestamp emailVerifiedAt;
    private String phoneNumber;
    private String description;
    private String profileImage = "default.jpg";

    private String role;
}

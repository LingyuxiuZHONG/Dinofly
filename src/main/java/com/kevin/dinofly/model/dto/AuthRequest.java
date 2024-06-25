package com.kevin.dinofly.model.dto;


import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}

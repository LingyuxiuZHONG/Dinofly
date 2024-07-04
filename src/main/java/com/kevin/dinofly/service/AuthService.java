package com.kevin.dinofly.service;


import com.kevin.dinofly.model.dto.LoginRequest;
import com.kevin.dinofly.model.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;


public interface AuthService {



    ResponseEntity<?> generateCode(String phone);

    ResponseEntity<?> register(RegisterRequest registerRequest);

    ResponseEntity<?> login(LoginRequest loginRequest);
}

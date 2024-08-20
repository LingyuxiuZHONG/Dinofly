package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.dto.LoginRequest;
import com.kevin.dinofly.model.dto.RegisterRequest;
import com.kevin.dinofly.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class AuthController {

    @Autowired
    private AuthService userService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){

        return userService.login(loginRequest);
    }

    @PostMapping("/code")
    public ResponseEntity<?> generateCode(@RequestBody String phone){
        return userService.generateCode(phone);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        return userService.register(registerRequest);
    }






}

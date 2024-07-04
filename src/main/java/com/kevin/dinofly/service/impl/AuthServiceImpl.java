package com.kevin.dinofly.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.kevin.dinofly.mapper.UserMapper;
import com.kevin.dinofly.model.dto.LoginRequest;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.model.dto.RegisterRequest;
import com.kevin.dinofly.service.AuthService;
import com.kevin.dinofly.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.sql.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String PHONE_REGEX = "^1[3456789]\\d{9}$";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    private static final String CODE_KEY_PREFIX = "dinofly:code:";

    private static final int CODE_LENGTH = 6;


    @Override
    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if(!registerRequest.getPhoneNumber().matches(PHONE_REGEX)){
            return ResponseEntity.ok("The phone number is not correct");
        }
        if(!registerRequest.getEmail().matches(EMAIL_REGEX)){
            return ResponseEntity.ok("The Email is not correct");
        }
        boolean phoneExists = userMapper.existsByPhone(registerRequest.getPhoneNumber());
        if (phoneExists) {
            return ResponseEntity.ok("The phone number has been used");
        }
        String code = stringRedisTemplate.opsForValue().get(CODE_KEY_PREFIX + registerRequest.getPhoneNumber());
        if(code == null){
            return ResponseEntity.ok("The code is expired");
        }
        if(!code.equals(registerRequest.getCode())){
            return ResponseEntity.ok("The code is not correct");
        }

        registerRequest.setCreatedAt(new Date(System.currentTimeMillis()));
        userMapper.save(registerRequest);
        String token = jwtUtil.generateToken(registerRequest.getUserId(),registerRequest.getRole());
        log.info("Bearer " + token);
        stringRedisTemplate.delete(CODE_KEY_PREFIX + registerRequest.getPhoneNumber());
        return ResponseEntity.ok("Register success");
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        User user = userMapper.findUserByUsername(loginRequest.getUsername());
        if(user == null || !user.getPassword().equals(loginRequest.getPassword())){
            return ResponseEntity.ok("Login failed");
        }
        String token = jwtUtil.generateToken(user.getUserId(),user.getRole());
        log.info(token);
        return ResponseEntity.ok("Login success");

    }

    @Override
    public ResponseEntity<?> generateCode(String phoneNumber) {
        if(!phoneNumber.matches(PHONE_REGEX)){
            return ResponseEntity.ok("The phone number is not correct");
        }
        boolean phoneExists = userMapper.existsByPhone(phoneNumber);
        if (phoneExists) {
            return ResponseEntity.ok("The phone number has been used");
        }

        String code = RandomUtil.randomNumbers(CODE_LENGTH);


        stringRedisTemplate.opsForValue().set(CODE_KEY_PREFIX + phoneNumber, code,3, TimeUnit.MINUTES);
        log.info(code);
        return ResponseEntity.ok("Code has sent");
    }



}

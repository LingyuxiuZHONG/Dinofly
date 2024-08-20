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
import org.springframework.http.HttpStatus;
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
        // 1. 验证手机号格式
        if(!registerRequest.getPhoneNumber().matches(PHONE_REGEX)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("手机号格式错误");
        }
        // 2. 验证邮箱格式
        if(!registerRequest.getEmail().matches(EMAIL_REGEX)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("邮箱格式错误");
        }
        // 3. 验证手机号是否存在
        boolean phoneExists = userMapper.existsByPhone(registerRequest.getPhoneNumber());
        if (phoneExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("手机号已被占用");
        }
        // 4. 验证验证码
        String code = stringRedisTemplate.opsForValue().get(CODE_KEY_PREFIX + registerRequest.getPhoneNumber());
        if(code == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码过期");
        }
        if(!code.equals(registerRequest.getCode())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("验证码错误");
        }
        // 5. 保存用户信息
        registerRequest.setCreatedAt(new Date(System.currentTimeMillis()));
        userMapper.save(registerRequest);
        // 6. 生成Token
        String token = jwtUtil.generateToken(registerRequest.getUserId(),registerRequest.getRole());
        log.info("Bearer " + token);
        // 7. 删除验证码缓存
        stringRedisTemplate.delete(CODE_KEY_PREFIX + registerRequest.getPhoneNumber());
        return ResponseEntity.ok("注册成功");
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        // 1. 查询数据库用户信息
        User user = userMapper.findUserByUsername(loginRequest.getUsername());
        // 2. 判断是否存在
        if(user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败，未找到用户");
        } else if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("登录失败，密码错误");
        }
        // 3. 存在，生成Token
        String token = jwtUtil.generateToken(user.getUserId(),user.getRole());
        log.info(token);
        return ResponseEntity.ok("登录成功");

    }

    @Override
    public ResponseEntity<?> generateCode(String phoneNumber) {
        // 1. 判断手机号格式
        if(!phoneNumber.matches(PHONE_REGEX)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("手机号格式错误");
        }
        // 2. 判断手机号是否存在
        boolean phoneExists = userMapper.existsByPhone(phoneNumber);
        if (phoneExists) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("手机号已被占用");
        }

        // 3. 生成验证码
        String code = RandomUtil.randomNumbers(CODE_LENGTH);
        // 4. 将验证码存入Redis
        stringRedisTemplate.opsForValue().set(CODE_KEY_PREFIX + phoneNumber, code,3, TimeUnit.MINUTES);
        log.info(code);
        return ResponseEntity.ok("验证码已发送");
    }



}

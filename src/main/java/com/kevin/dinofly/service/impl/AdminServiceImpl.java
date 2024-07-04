package com.kevin.dinofly.service.impl;

import com.kevin.dinofly.mapper.AdMapper;
import com.kevin.dinofly.mapper.UserMapper;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdMapper adMapper;

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        userMapper.deleteUser(id);
        return ResponseEntity.ok("User: " + id + " is deleted successfully");
    }



    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userMapper.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<?> deleteAd(Long id) {
        adMapper.deleteAd(id);
        return ResponseEntity.ok("Ad: " + id + " is deleted successfully");
    }
}

package com.kevin.dinofly.service.impl;

import com.kevin.dinofly.mapper.AdMapper;
import com.kevin.dinofly.mapper.UserMapper;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.service.AdminService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;



@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdMapper adMapper;


    private static final List<String> roleList = List.of("Landlord","Tenant","Admin");

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        try {
            int rowsAffected = userMapper.deleteUser(id);
            if(rowsAffected == 0){
                log.warn("尝试删除不存在的用户: {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("用户: " + id + "不存在");
            }
            log.info("成功删除用户: {}", id);
            return ResponseEntity.ok("用户:" + id + "成功删除");


        }catch (Exception e){
            log.error("删除用户时发生错误: {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除用户时发生错误");

        }
    }



    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userMapper.findAllUsers();
        if (allUsers.isEmpty()) {
            log.warn("没有找到用户");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("没有找到用户");
        }
        return ResponseEntity.ok(allUsers);
    }

    @Override
    public ResponseEntity<?> deleteAd(Long id) {
        try {
            int rowsAffected = adMapper.deleteAd(id);
            if (rowsAffected == 0) {
                log.warn("尝试删除不存在的房源：{}",id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("房源: " + id + "不存在");
            }
            log.info("成功删除房源：{}",id);
            return ResponseEntity.ok("房源: " + id + "成功删除");
        } catch (Exception e) {
            log.error("删除房源时发生错误: {}",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除房源时发生错误");
        }
    }

    @Override
    public ResponseEntity<?> changeUserRole(Long id, String role) {
        if(!roleList.contains(role)){
            log.warn("无效角色类：{}",role);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("无效角色类");
        }
        userMapper.changeUserRole(id,role);
        log.info("成功修改用户: {} ，为角色：{}",id,role);
        return ResponseEntity.ok("用户: " + id + "的角色已被成功修改为" + role);

    }
}

package com.kevin.dinofly.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.kevin.dinofly.mapper.UserMapper;
import com.kevin.dinofly.model.AuthRequest;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.model.UserDTO;
import com.kevin.dinofly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User authenticate(AuthRequest authRequest){
        User user = userMapper.findUserByUsername(authRequest.getUsername());
        if(user != null && user.getPassword().equals(authRequest.getPassword())){
            return user;
        }
        return null;
    }



    @Override
    public boolean findByUsername(String username) {
        return userMapper.findUserByUsername(username) != null ? true : false;
    }

    @Override
    public Long save(User user) {
        userMapper.save(user);
        return user.getId();
    }

    @Override
    public UserDTO get(Long id) {
        User user = userMapper.findUserById(id);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public List<User> getUsers() {

        return userMapper.findAllUsers();
    }

    @Override
    public void deleteUser(Long id) {
        userMapper.delete(id);
    }

    @Override
    public void update(User user) {
        userMapper.updateUser(user);
    }


}

package com.kevin.dinofly.service;


import com.kevin.dinofly.model.AuthRequest;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.model.UserDTO;

import java.util.List;


public interface UserService {


    User authenticate(AuthRequest authRequest);



    boolean findByUsername(String username);

    Long save(User user);

    UserDTO get(Long id);

    List<User> getUsers();


    void deleteUser(Long id);

    void update(User user);
}

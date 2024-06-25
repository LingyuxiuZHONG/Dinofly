package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.User;
import com.kevin.dinofly.service.AdService;
import com.kevin.dinofly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdService adService;

    /*
     * User Management
     *
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody User user){
        user.setUserId(id);
        userService.update(user);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }



    /*
     * Ad Management
     *
     */

    @DeleteMapping("/ads/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id){
        adService.deleteAd(id);
        return ResponseEntity.ok(null);
    }



}

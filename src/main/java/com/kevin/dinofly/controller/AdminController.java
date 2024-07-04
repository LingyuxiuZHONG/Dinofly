package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.User;
import com.kevin.dinofly.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    /*
     * User Management
     *
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        return adminService.deleteUser(id);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(){
        return adminService.getAllUsers();
    }



    /*
     * Ad Management
     *
     */

    @DeleteMapping("/ads/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id){
        return adminService.deleteAd(id);
    }



}

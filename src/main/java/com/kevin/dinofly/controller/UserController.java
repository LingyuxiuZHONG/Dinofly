package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.User;
import com.kevin.dinofly.model.UserDTO;
import com.kevin.dinofly.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@PathVariable Long id) {
        UserDTO user = userService.get(id);

        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return new ResponseEntity<>("Cannot get user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

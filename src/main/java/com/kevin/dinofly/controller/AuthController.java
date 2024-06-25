package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.dto.AuthRequest;
import com.kevin.dinofly.model.dto.AuthResponse;
import com.kevin.dinofly.model.User;
import com.kevin.dinofly.service.UserService;
import com.kevin.dinofly.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) throws Exception {
        User user = userService.authenticate(authRequest);

        if(user != null){
            String token = jwtUtil.generateToken(user.getUserId(),user.getRole());
            return ResponseEntity.ok(new AuthResponse(token));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        if(userService.findByUsername(user.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        Long id = userService.save(user);

        String token = jwtUtil.generateToken(id,user.getRole());
        return ResponseEntity.ok(new AuthResponse(token));


    }




}

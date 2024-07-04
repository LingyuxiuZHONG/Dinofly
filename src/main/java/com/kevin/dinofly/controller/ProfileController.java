package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.dto.ProfileUpdate;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private ProfileService profileService;



    @GetMapping()
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return profileService.getUser(userDetails.getId());

    }


    @PutMapping ()
    public ResponseEntity<?> changeUserInfo(@RequestBody ProfileUpdate userInfo, @AuthenticationPrincipal CustomUserDetails userDetails){
        return profileService.updateUser(userInfo,userDetails);
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal CustomUserDetails userDetails){
        return profileService.uploadImage(file,userDetails);

    }




}

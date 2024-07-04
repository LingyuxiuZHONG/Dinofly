package com.kevin.dinofly.service;

import com.kevin.dinofly.model.dto.ProfileUpdate;
import com.kevin.dinofly.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileService {
    ResponseEntity<?> getUser(Long id);

    ResponseEntity<?> updateUser(ProfileUpdate userInfo, CustomUserDetails userDetails);

    ResponseEntity<?> uploadImage(MultipartFile file, CustomUserDetails userDetails);
}

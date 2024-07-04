package com.kevin.dinofly.service;

import com.kevin.dinofly.model.User;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    ResponseEntity<?> deleteUser(Long id);

    ResponseEntity<?> getAllUsers();

    ResponseEntity<?> deleteAd(Long id);
}

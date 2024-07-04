package com.kevin.dinofly.service;

import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdService {
    Long save(Ad ad, CustomUserDetails userDetails);

    void deleteAd(Long id);

    ResponseEntity<?> getAdById(Long id);

    ResponseEntity<?> getAdByIdWithMutex(Long id);

    List<Ad> getAllAds();


    void updateAd(Ad ad);


    List<Ad> getAllAdsAdmin();
}

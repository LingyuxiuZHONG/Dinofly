package com.kevin.dinofly.service;

import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.model.dto.AdDTO;

import java.util.List;

public interface AdService {
    Long save(Ad ad);

    void deleteAd(Long id);

    AdDTO getAdById(Long id);

    List<AdDTO> getAllAds();


    void updateAd(Ad ad);


    List<Ad> getAllAdsAdmin();
}

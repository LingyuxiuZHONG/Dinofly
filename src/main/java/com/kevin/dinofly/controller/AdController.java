package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;


    @PostMapping("")
    public ResponseEntity<?> createAd(@RequestBody Ad ad, @AuthenticationPrincipal CustomUserDetails userDetails){
        Long id = adService.save(ad,userDetails);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.ok(null);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody Ad ad) {
        ad.setAdId(id);
        adService.updateAd(ad);
        return ResponseEntity.ok(null);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getAd(@PathVariable Long id){
        return adService.getAdById(id);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllAds(){
        List<Ad> adList = adService.getAllAds();
        return ResponseEntity.ok(adList);
    }



}


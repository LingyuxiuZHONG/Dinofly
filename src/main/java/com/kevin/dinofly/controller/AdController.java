package com.kevin.dinofly.controller;


import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.model.AdDTO;
import com.kevin.dinofly.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;


    @PostMapping("")
    public ResponseEntity<?> createAd(@RequestBody Ad ad){
        Long id = adService.save(ad);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
        return ResponseEntity.ok(null);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable Long id, @RequestBody Ad ad) {
        ad.setId(id);
        adService.updateAd(ad);
        return ResponseEntity.ok(null);
    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getAd(@PathVariable Long id){
        AdDTO adDTO = adService.getAdById(id);
        return ResponseEntity.ok(adDTO);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllAds(){
        List<AdDTO> adDTOList = adService.getAllAds();
        return ResponseEntity.ok(adDTOList);
    }



}


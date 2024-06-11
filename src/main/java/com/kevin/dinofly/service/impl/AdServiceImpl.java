package com.kevin.dinofly.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.kevin.dinofly.mapper.AdMapper;
import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.model.AdDTO;
import com.kevin.dinofly.service.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdMapper adMapper;

    @Override
    public Long save(Ad ad) {
        adMapper.save(ad);
        return ad.getId();
    }

    @Override
    public void deleteAd(Long id) {
        adMapper.deleteAd(id);
    }

    @Override
    public AdDTO getAdById(Long id) {
        Ad ad = adMapper.findAdById(id);
        AdDTO adDTO = BeanUtil.copyProperties(ad,AdDTO.class);
        return adDTO;
    }

    @Override
    public List<AdDTO> getAllAds() {
        List<Ad> ads = adMapper.findAllAds();
        return ads.stream()
                .map(ad -> BeanUtil.copyProperties(ad, AdDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateAd(Ad ad) {
        adMapper.updateAd(ad);
    }

    @Override
    public List<Ad> getAllAdsAdmin() {
        return adMapper.findAllAds();
    }
}

package com.kevin.dinofly.service.impl;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.json.JSONUtil;
import com.kevin.dinofly.mapper.AdMapper;
import com.kevin.dinofly.model.Ad;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.AdService;
import com.kevin.dinofly.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class AdServiceImpl implements AdService {

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String AD_KEY_PREFIX = Constants.AD_KEY_PREFIX;

    private static final int AD_EXPIRE_TIME = 30 * 60;

    private static final String LOCK_AD_KEY = "dinofly:lock:ad";

    private static final int AD_NULL_EXPIRE_TIME = 3 * 60;

    @Override
    public Long save(Ad ad, CustomUserDetails userDetails) {
            ad.setOwnerId(userDetails.getId());
            adMapper.save(ad);
            return ad.getAdId();

    }

    @Override
    public void deleteAd(Long id) {
        adMapper.deleteAd(id);
    }

    @Override
    public ResponseEntity<?> getAdById(Long id) {
        if(id == null){
            return ResponseEntity.ok("The ad id is empty!");
        }
        String adJson = stringRedisTemplate.opsForValue().get(AD_KEY_PREFIX + id);
        Ad ad;
        if(adJson != null){
            return ResponseEntity.ok(JSONUtil.toBean(adJson,Ad.class));
        }
        ad = adMapper.findAdById(id);
        if(ad == null){
            stringRedisTemplate.opsForValue().set(AD_KEY_PREFIX + "id","",AD_NULL_EXPIRE_TIME,TimeUnit.SECONDS);
            return ResponseEntity.ok("The ad can not be found!");
        }
        stringRedisTemplate.opsForValue().set(AD_KEY_PREFIX + id, JSONUtil.toJsonStr(ad),AD_EXPIRE_TIME,TimeUnit.SECONDS);
        return ResponseEntity.ok(ad);

    }

    @Override
    public ResponseEntity<?> getAdByIdWithMutex(Long id) {
        if(id == null){
            return ResponseEntity.ok("The ad id is empty!");
        }
        String adJson = stringRedisTemplate.opsForValue().get(AD_KEY_PREFIX + id);
        Ad ad;
        if(adJson != null){
            return ResponseEntity.ok(JSONUtil.toBean(adJson,Ad.class));
        }

        try {
            //否则去数据库中查
            boolean flag = tryLock(LOCK_AD_KEY + id);
            if (!flag) {
                Thread.sleep(50);
                return getAdByIdWithMutex(id);
            }
            ad = adMapper.findAdById(id);
            if(ad == null){
                stringRedisTemplate.opsForValue().set(AD_KEY_PREFIX + "id","",AD_EXPIRE_TIME,TimeUnit.SECONDS);
                return ResponseEntity.ok("The ad can not be found!");
            }
            stringRedisTemplate.opsForValue().set(AD_KEY_PREFIX + id, JSONUtil.toJsonStr(ad),AD_EXPIRE_TIME,TimeUnit.SECONDS);
            return ResponseEntity.ok(ad);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            unlock(LOCK_AD_KEY + id);
        }
    }

    private boolean tryLock(String key) {
        Boolean flag = stringRedisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        //避免返回值为null，我们这里使用了BooleanUtil工具类
        return BooleanUtil.isTrue(flag);
    }

    private void unlock(String key) {
        stringRedisTemplate.delete(key);
    }

    @Override
    public List<Ad> getAllAds() {
        List<Ad> ads = adMapper.findAllAds();
        return ads;
    }

    @Override
    public void updateAd(Ad ad) {
        adMapper.updateAd(ad);
        stringRedisTemplate.delete(AD_KEY_PREFIX + ad.getAdId());
    }

    @Override
    public List<Ad> getAllAdsAdmin() {
        return adMapper.findAllAds();
    }
}

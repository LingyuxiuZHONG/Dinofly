package com.kevin.dinofly.mapper;


import com.kevin.dinofly.model.Ad;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AdMapper {

    void save(Ad ad);

    Ad findAdById(Long id);

    @Delete("DELETE FROM ad WHERE id = #{id}")
    int deleteAd(Long id);


    List<Ad> findAllAds();

    void updateAd(Ad ad);
}

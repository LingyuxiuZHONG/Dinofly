package com.kevin.dinofly.mapper;


import cn.hutool.core.date.DateTime;
import com.kevin.dinofly.model.Reservation;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface ReservationMapper {
    void createReservation(Reservation reservation);

    Reservation getReservationById(String id);

    void updateReservationStatus(@Param("id") String id, @Param("status") String status);

    boolean isOverlappingReservationExists(long adId, Date startDate, Date endDate);

//    @Update({
//            "<script>",
//            "UPDATE reservation",
//            "SET status = #{status}",
//            "WHERE id IN",
//            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>",
//            "#{id}",
//            "</foreach>",
//            "</script>"
//    })
//    void updateReservationsStatus(@Param("ids") List<String> ids, @Param("status") String status);


}

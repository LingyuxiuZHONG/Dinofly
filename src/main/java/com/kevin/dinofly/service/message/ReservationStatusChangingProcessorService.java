package com.kevin.dinofly.service.message;


import com.kevin.dinofly.mapper.ReservationMapper;
import com.kevin.dinofly.util.Constants;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ReservationStatusChangingProcessorService extends AbstractMessageProcessorService{


    @Autowired
    private ReservationMapper reservationMapper;

    @PostConstruct
    public void init() {
        this.streamKey = Constants.RESERVATION_EXPIRE_STREAM_KEY;
        this.groupName = Constants.RESERVATION_GROUP_NAME;
        this.consumerName = Constants.RESERVATION_CONSUMER_NAME;
        super.init();
    }
    @Override
    protected void processMessageBody(Map<String, Object> message) {
        reservationMapper.updateReservationStatus((String) message.get("reservationId"), (String) message.get("status"));
    }
}

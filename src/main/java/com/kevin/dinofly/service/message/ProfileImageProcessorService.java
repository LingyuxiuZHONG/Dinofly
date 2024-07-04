package com.kevin.dinofly.service.message;

import com.kevin.dinofly.mapper.UserMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class ProfileImageProcessorService extends AbstractMessageProcessorService{
    @Autowired
    private UserMapper userMapper;

    @PostConstruct
    public void init(){
        this.streamKey = "dinofly:profile:imageStream";
        this.groupName = "dinofly:profile:group";
        this.consumerName = "dinofly:profile:consumer";
        super.init();
    }
    @Override
    protected void processMessageBody(Map<String, Object> message) {
        userMapper.uploadProfileImage((Long) message.get("userId"),(String)message.get("imageName"));
    }
}

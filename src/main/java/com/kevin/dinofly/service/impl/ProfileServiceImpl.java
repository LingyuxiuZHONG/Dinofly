package com.kevin.dinofly.service.impl;

import cn.hutool.json.JSONUtil;
import com.kevin.dinofly.mapper.UserMapper;
import com.kevin.dinofly.model.dto.ProfileUpdate;
import com.kevin.dinofly.model.dto.ProfileDisplay;
import com.kevin.dinofly.security.CustomUserDetails;
import com.kevin.dinofly.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String USER_KEY_PREFIX = "dinofly:user:";

    private static final String PHONE_REGEX = "^1[3456789]\\d{9}$";

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

    private static final String CODE_KEY_PREFIX = "dinofly:code:";

    private static final String UPLOAD_DIR = "src/main/resources/uploads/";

    private static final String PROFILE_IMAGE_STREAM_KEY = "dinofly:profile:imageStream";


    @Override
    public ResponseEntity<?> getUser(Long id) {
        ProfileDisplay user = userMapper.findUserInfoById(id);
        stringRedisTemplate.opsForValue().set(USER_KEY_PREFIX + id, JSONUtil.toJsonStr(user));
        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity<?> updateUser(ProfileUpdate userInfo, CustomUserDetails userDetails) {
        userInfo.setUserId(userDetails.getId());
        switch (userInfo.getFieldUpdate()){
            case "username":
                boolean existsUsername = userMapper.existsByUsername(userInfo.getUsername());
                if(existsUsername){
                    return ResponseEntity.ok("This username has been used");
                }
                break;
            case "password":
                boolean verifyPassword = passwordVerify(userInfo.getCurrentPassword(), userDetails.getPassword());
                if(!verifyPassword){
                    return ResponseEntity.ok("Password is not correct");
                }
                userMapper.updateUserPassword(userInfo.getUserId(),userInfo.getPassword());
                break;
            case "email":
                if(!userInfo.getEmail().matches(EMAIL_REGEX))
                break;
            case "phone number":
                if(!userInfo.getPhoneNumber().matches(PHONE_REGEX)){
                    return ResponseEntity.ok("The phone number is not correct");
                }
                boolean phoneExists = userMapper.existsByPhone(userInfo.getPhoneNumber());
                if (phoneExists) {
                    return ResponseEntity.ok("The phone number has been used");
                }
                String code = stringRedisTemplate.opsForValue().get(CODE_KEY_PREFIX + userInfo.getPhoneNumber());
                if(code == null){
                    return ResponseEntity.ok("The code is expired");
                }
                if(!code.equals(userInfo.getCode())){
                    return ResponseEntity.ok("The code is not correct");
                }
                userMapper.updatePhoneNumber(userInfo.getUserId(),userInfo.getPhoneNumber());
                break;
            case "description":
                userMapper.updateDescription(userInfo.getUserId(),userInfo.getDescription());
                break;
            case "sex":
                userMapper.updateSex(userInfo.getUserId(),userInfo.getSex());
                break;

        }
        return ResponseEntity.ok("Update successfully");
    }

    @Override
    public ResponseEntity<?> uploadImage(MultipartFile file, CustomUserDetails userDetails) {
        if (file.isEmpty()) {
            return ResponseEntity.ok("Please upload a file");
        }
        try {
            String fileName = "profile_image_" + userDetails.getId() + ".png";
            Path uploadPath = Paths.get(UPLOAD_DIR);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path path = uploadPath.resolve(fileName);
            Files.write(path, file.getBytes());

            Map<String,String> map = new HashMap<>();
            map.put("userId", String.valueOf(userDetails.getId()));
            map.put("imageName",fileName);
            stringRedisTemplate.opsForStream().add(PROFILE_IMAGE_STREAM_KEY, map);
            return ResponseEntity.ok("Upload success");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.ok("Upload failed");
        }
    }

    private boolean passwordVerify(String password, String p) {
        if(!password.equals(p)){
            return false;
        }else{
            return true;
        }
    }
}

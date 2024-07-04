package com.kevin.dinofly.mapper;


import com.kevin.dinofly.model.User;
import com.kevin.dinofly.model.dto.ProfileDisplay;
import com.kevin.dinofly.security.CustomUserDetails;
import org.apache.ibatis.annotations.*;

import java.sql.Date;
import java.util.List;


@Mapper
public interface UserMapper {

    @Select("SELECT id AS userId, username, password FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("SELECT id AS userId, username, email, phone_number AS phoneNumber, description, profile_image AS profileImage, sex, created_at AS createAt FROM user WHERE id = #{userId}")
    ProfileDisplay findUserInfoById(Long userId);

    @Select("SELECT id AS userId, username, password, role FROM user WHERE id = #{userId}")
    CustomUserDetails findUserDetailsById(Long userId);

    @Insert("INSERT INTO user (username, email, password, phone_number, description, profile_image, role, sex, created_at) " +
            "VALUES (#{username}, #{email}, #{password}, #{phoneNumber}, #{description}, #{profileImage}, #{role}, #{sex}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void save(User user);

    @Select("SELECT * FROM user")
    List<User> findAllUsers();

    @Delete("DELETE FROM user where id = #{id}")
    void deleteUser(Long id);


    boolean existsByPhone(String phoneNumber);

    boolean existsByUsername(String username);

    @Update("UPDATE user SET password = #{password} where id = #{userId}")
    void updateUserPassword(Long userId, String password);

    @Update("UPDATE user SET description = #{description} where id = #{userId}")
    void updateDescription(Long userId, String description);

    @Update("UPDATE user SET sex = #{sex} where id = #{userId}")
    void updateSex(Long userId, String sex);

    @Update("UPDATE user SET phone_number = #{phoneNumber} where id = #{userId}")
    void updatePhoneNumber(Long userId, String phoneNumber);

    @Update("UPDATE user SET profile_image = #{imageName} where id = #{userId}")
    void uploadProfileImage(Long userId, String imageName);
}
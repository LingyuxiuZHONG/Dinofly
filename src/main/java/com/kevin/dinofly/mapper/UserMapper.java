package com.kevin.dinofly.mapper;


import com.kevin.dinofly.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {

    @Select("SELECT id, username, password FROM user WHERE username = #{username}")
    User findUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findUserById(Long id);

    @Insert("INSERT INTO user (username, email, password, phone_number, description, profile_image,role) " +
            "VALUES (#{username}, #{email}, #{password}, #{phoneNumber}, #{description}, #{profileImage},#{role})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void save(User user);

    @Select("SELECT * FROM user")
    List<User> findAllUsers();

    @Delete("DELETE FROM user where id = #{id}")
    void delete(Long id);

    void updateUser(User user);
}
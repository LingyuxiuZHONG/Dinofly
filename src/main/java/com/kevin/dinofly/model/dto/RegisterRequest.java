package com.kevin.dinofly.model.dto;

import com.kevin.dinofly.model.User;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;


@Data
public class RegisterRequest extends User {
    private String code;
}

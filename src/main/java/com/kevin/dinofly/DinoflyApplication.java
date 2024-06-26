package com.kevin.dinofly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.kevin.dinofly.mapper")
public class DinoflyApplication {

    public static void main(String[] args) {
        SpringApplication.run(DinoflyApplication.class, args);
    }

}

package com.statusmetro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StatusMetroApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatusMetroApplication.class, args);
    }
}

package com.example.traveler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TravelerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelerApplication.class, args);
    }

}

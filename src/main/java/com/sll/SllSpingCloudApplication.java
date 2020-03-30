package com.sll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SllSpingCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(SllSpingCloudApplication.class, args);
    }
}

package com.reservoir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableAsync
@SpringBootApplication
public class MinApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MinApplication.class, args);
    }

}

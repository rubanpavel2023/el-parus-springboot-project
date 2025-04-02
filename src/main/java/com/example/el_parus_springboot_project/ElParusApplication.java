package com.example.el_parus_springboot_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ElParusApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElParusApplication.class, args);
    }

}

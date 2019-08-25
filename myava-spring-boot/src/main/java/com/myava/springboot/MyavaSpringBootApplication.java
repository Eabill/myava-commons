package com.myava.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.myava" })
public class MyavaSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyavaSpringBootApplication.class, args);
    }
}

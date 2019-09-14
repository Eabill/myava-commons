package com.myava.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@EnableScheduling
@SpringBootApplication(scanBasePackages = { "com.myava" })
@MapperScan("com.myava.springboot.mapper")
public class MyavaSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyavaSpringBootApplication.class, args);
    }
}

package com.cuckoo.pomelo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "com.cuckoo.pomelo.**" })
@org.mybatis.spring.annotation.MapperScan({ "com.cuckoo.pomelo.**.mapper" })
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
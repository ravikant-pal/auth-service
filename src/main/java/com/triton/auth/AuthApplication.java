package com.triton.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@EnableMongoAuditing
@SpringBootApplication
@ComponentScan(basePackages = "com.triton.auth")
@EnableMongoRepositories(basePackages = "com.triton.auth")
public class AuthApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.info("============================================================================");
        log.info("========================= Auth Application is Up!!! ========================");
        log.info("============================================================================");
    }
}

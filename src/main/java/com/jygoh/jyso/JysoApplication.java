package com.jygoh.jyso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JysoApplication {

    public static void main(String[] args) {
        SpringApplication.run(JysoApplication.class, args);
    }

}

package com.rtbplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class RtbPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(RtbPlatformApplication.class, args);
    }
}

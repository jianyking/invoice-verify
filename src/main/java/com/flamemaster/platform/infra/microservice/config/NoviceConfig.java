package com.flamemaster.platform.infra.microservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NoviceConfig {

    @Value("${novice.check.url}")
    private String CHECK_URL;

}

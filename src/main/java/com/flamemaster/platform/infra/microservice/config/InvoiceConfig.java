package com.flamemaster.platform.infra.microservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class InvoiceConfig {

    @Value("${novice.check.url}")
    private String checkUrl;

    @Value("${novice.code.url}")
    private String genCodeUrl;

    @Value("${novice.captch.check.url}")
    private String checkCaptchUrl;

    @Value("${novice.tesseract.path}")
    private String tesseractPath;

    @Value("${novice.image.path}")
    private String imageTempPath;

    @Value("${novice.mode}")
    private String mode;
}

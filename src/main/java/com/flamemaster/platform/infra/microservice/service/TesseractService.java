package com.flamemaster.platform.infra.microservice.service;


import java.awt.image.BufferedImage;

public interface TesseractService {
    String doOCR(String imageFullName, String language);

    String doOCR(BufferedImage bufferedImage, String language);
}

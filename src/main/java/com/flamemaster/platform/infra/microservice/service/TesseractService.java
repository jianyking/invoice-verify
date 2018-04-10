package com.flamemaster.platform.infra.microservice.service;


public interface TesseractService {
    String doOCR(String imageFullName, String language);
}

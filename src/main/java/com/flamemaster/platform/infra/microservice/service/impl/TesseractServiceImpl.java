package com.flamemaster.platform.infra.microservice.service.impl;

import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.FileUtils;
import com.flamemaster.platform.infra.microservice.service.TesseractService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Random;

@Log4j
@Service
public class TesseractServiceImpl implements TesseractService {

    @Resource
    private InvoiceConfig invoiceConfig;

    private static Random random = new Random();

    public String doOCR(String imageFullName, String language) {
        try {
            String[] cmd = new String[]{invoiceConfig.getTesseractPath() + "tesseract", imageFullName, imageFullName, "-l", language};
            Process pb = Runtime.getRuntime().exec(cmd);
            int execRes = pb.waitFor();
            if (execRes != 0) {
                throw new RuntimeException("Command execution failed, return " + execRes);
            }
            return FileUtils.readFileByChars(imageFullName + ".txt", "UTF-8");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
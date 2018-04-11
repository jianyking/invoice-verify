package com.flamemaster.platform.infra.microservice.service.impl;

import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.HttpUtil;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.ImageUtils;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.TesseractService;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private InvoiceConfig invoiceConfig;

    @Resource
    private TesseractService tesseractService;

    private static Random random = new Random();

    private boolean verificationCaptch(String captch) {
        String url = invoiceConfig.getCheckCaptchUrl();
        try {
            Map<String, Object> reqParam = new HashMap<>();
            reqParam.put("yzm", captch);
            String checkCaptchRes = HttpUtil.postByForm(reqParam, url);
            log.info("验证码验证结果：" + checkCaptchRes);
            return "success".equals(checkCaptchRes);
        } catch (IOException e) {
            log.error("验证验证码正确性失败", e);
            return false;
        }
    }

    public Entity identifyCaptcha(int recallCount) {
        if(recallCount <= 0) {
            return new Entity(InvoiceConstants.FAIL_STATUS, "识别失败, 超过允许的最多错误次数", null);
        }
        try {
            int tmp = Math.abs(random.nextInt() % 1000);
            String url = invoiceConfig.getGenCodeUrl() + "?tmp=" + tmp;
            BufferedImage bufferedImage = ImageUtils.loadImage(new URL(url));
            bufferedImage = ImageUtils.cleanCaptcha(bufferedImage);
            String randomName = "" + System.currentTimeMillis() + Math.abs(random.nextInt() % 10000) + ".jpg";
            String imageFullName = invoiceConfig.getImageTempPath() + randomName;
            ImageUtils.saveImage(imageFullName, bufferedImage, "jpg");
            String captcha = tesseractService.doOCR(imageFullName, "eng");
            StringBuilder sb = new StringBuilder();
            for(char c : captcha.toCharArray()) {
                if((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                    sb.append(c);
                }
            }
            log.info("验证码是：" + sb.toString());
            if(sb.length() != 4 || !verificationCaptch(sb.toString().toLowerCase())) {
                return identifyCaptcha(recallCount - 1);
            }
            return new Entity(InvoiceConstants.SUCCESS_STATUS, "识别成功", sb.toString().toLowerCase());
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return new Entity(InvoiceConstants.FAIL_STATUS, "识别失败", null);
        }
    }
}

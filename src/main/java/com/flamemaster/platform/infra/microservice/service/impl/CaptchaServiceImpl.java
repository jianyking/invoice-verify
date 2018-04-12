package com.flamemaster.platform.infra.microservice.service.impl;

import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.FileUtils;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.HttpUtil;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.ImageUtils;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.StringUtils;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.TesseractService;
import com.flamemaster.platform.infra.microservice.wrapper.CaptchaWrapper;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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

    private static String captchaCahce = "1-1";

    private BufferedImage loadCaptcha() throws IOException {
        int tmp = Math.abs(random.nextInt() % 1000);
        String url = invoiceConfig.getGenCodeUrl() + "?tmp=" + tmp;
        return ImageUtils.loadImage(new URL(url));
    }

    private boolean verificationCaptch(String captch) {
        String url = invoiceConfig.getCheckCaptchUrl();
        try {
            Map<String, Object> reqParam = new HashMap<>();
            reqParam.put("yzm", captch);
            String checkCaptchRes = HttpUtil.postByForm(reqParam, url);
            log.info("验证码验证结果：" + ("success".equals(checkCaptchRes) ? "成功" : "失败"));
            return "success".equals(checkCaptchRes);
        } catch (IOException e) {
            log.error("验证验证码正确性失败", e);
            return false;
        }
    }

    public Entity identifyCaptcha(int recallCount) {
        try {
            if(checkCaptchaCache()) {
                return new Entity(InvoiceConstants.SUCCESS_STATUS, "识别成功", captchaCahce.split("-")[0]);
            }
            String captch = reTryIdentifyCaptcha(recallCount);
            captchaCahce = captch + "-" + System.currentTimeMillis();
            return new Entity(InvoiceConstants.SUCCESS_STATUS, "识别成功", captch);
        } catch (RuntimeException re) {
            log.error(re.getMessage());
            return new Entity(InvoiceConstants.FAIL_STATUS, "识别验证码超过最大的可重试次数", null);
        }
    }

    private String reTryIdentifyCaptcha(int reTryTimes) {
        for (int i = 0; i < reTryTimes; i++) {
            try {
                String captcha = doOnceIdentifyCaptcha();
                if(checkCaptcha(captcha)) {
                    return captcha;
                } else {
                    log.error("验证码识别失败");
                }
            } catch (Exception e) {
                log.error("验证码识别报错", e);
            }
        }
        throw new RuntimeException("识别验证码超过最大的可重试次数");
    }

    private String doOnceIdentifyCaptcha() throws IOException {
        BufferedImage bi = loadCaptcha();
        if (bi == null) {
            return null;
        }
        String tempImageName = invoiceConfig.getImageTempPath() + System.currentTimeMillis() + random.nextInt(10000) + ".jpg";
        CaptchaWrapper cw = new CaptchaWrapper(bi);
        cw.adjustContrast(1.3).binarization(0).cleanByEightBitDomain(5, 5, 0)
                .cleanIsland(7).cleanSide(3, 0).save(tempImageName);
        log.info("生成的临时验证码图片名称: " + tempImageName);
        String captcha = tesseractService.doOCR(tempImageName, InvoiceConstants.TESSERACT_OCR_LANGUAGE);
        if (InvoiceConstants.RUN_MODE_NORMAL.equals(invoiceConfig.getMode())) {
            if (!FileUtils.deleteFile(tempImageName) || !FileUtils.deleteFile(tempImageName + ".txt")) {
                log.error("删除临时文件失败!");
            }
        }
        captcha = StringUtils.cleanToNumberOrLetterString(captcha);
        log.info("获取到的验证码是：" + captcha);
        return captcha;
    }

    private boolean checkCaptcha(String captcha) {
        return captcha != null && captcha.length() == 4 && verificationCaptch(captcha);
    }

    private boolean checkCaptchaCache() {
        String[] cache = captchaCahce.split("-");
        long time = Long.parseLong(cache[1]);
        return (System.currentTimeMillis() - time < InvoiceConstants.CAPTCHA_CACHE_FAILURE_TIME) && checkCaptcha(cache[0]);
    }
}

package com.flamemaster.platform.infra.microservice.service.impl;

import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.HttpUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Log4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private InvoiceConfig invoiceConfig;

    private static Random random = new Random();

    private InputStream getCaptcha() {
        int tmp = Math.abs(random.nextInt() % 1000);
        String url = invoiceConfig.getGenCodeUrl() + "?tmp=" + tmp;
        try {
            return HttpUtil.getStream(url);
        } catch (IOException e) {
            log.error("获取验证码失败", e);
            return null;
        }
    }

    public Entity identifyCaptcha() {
        //TODO
        return new Entity(InvoiceConstants.SUCCESS_STATUS, "识别成功", "fgfq");
    }

}

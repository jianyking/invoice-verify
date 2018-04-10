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

@Log4j
@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Resource
    private InvoiceConfig invoiceConfig;

    public Entity getCaptcha() {
        String url = invoiceConfig.getGenCodeUrl();
        String response;
        try {
            response = HttpUtil.get(url);
        } catch (IOException e) {
            log.error("获取验证码失败", e);
            return new Entity(InvoiceConstants.FAIL_STATUS, "获取验证码失败", null);
        }
        return new Entity(InvoiceConstants.SUCCESS_STATUS, "获取验证码成功", response);
    }

    public Entity identifyCaptcha(String base64code) {
        //TODO
        return new Entity(InvoiceConstants.SUCCESS_STATUS, "识别成功", "AABB");
    }

}

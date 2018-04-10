package com.flamemaster.platform.infra.microservice.service;

import com.flamemaster.platform.infra.microservice.base.Entity;

import java.io.InputStream;

public interface CaptchaService {

    /**
     * 获取识别的验证码
     * @return 识别结果
     */
    Entity identifyCaptcha(int recallCount);

}

package com.flamemaster.platform.infra.microservice.service;

import com.flamemaster.platform.infra.microservice.base.Entity;

public interface CaptchaService {

    /**
     * 获取新的验证码
     * @return 获取到的验证码
     */
    Entity getCaptcha();

}

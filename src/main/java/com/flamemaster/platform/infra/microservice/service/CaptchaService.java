package com.flamemaster.platform.infra.microservice.service;

import com.flamemaster.platform.infra.microservice.base.Entity;

public interface CaptchaService {

    /**
     * 获取新的验证码
     * @return 获取到的验证码
     */
    Entity getCaptcha();

    /**
     * 识别验证码
     * @param base64code 图片的base64数据
     * @return 识别结果
     */
    Entity identifyCaptcha(String base64code);

}

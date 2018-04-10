package com.flamemaster.platform.infra.microservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.base.InvoiceData;
import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceResponse;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import com.flamemaster.platform.infra.microservice.common.infrastruct.utils.HttpUtil;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;

@Log4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Resource
    private InvoiceConfig invoiceConfig;

    @Resource
    private CaptchaService captchaService;

    private static final String CAPTCHA_ERROR_MSG = "验证码错误！";

    /**
     * 查询发票信息
     * @param request 查询参数
     * @return 发票信息
     */
    public InvoiceResponse queryInvoice(InvoiceRequest request) {

        if (StringUtils.isEmpty(request.getInvoiceCode())
                || StringUtils.isEmpty(request.getInvoiceNo())
                || StringUtils.isEmpty(request.getInvoiceDate())
                || StringUtils.isEmpty(request.getInvoiceAmount())) {
            return new InvoiceResponse(InvoiceConstants.PARAM_NOT_VALID, "参数不符合规范", null, null);
        }

        String checkUrl = invoiceConfig.getCheckUrl();
        Entity entity = captchaService.identifyCaptcha();
        if (entity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
            request.setValidCode(entity.getData());
            String requestJson = JSON.toJSONString(request);

            log.info("调用查询接口 入参: " + requestJson);
            long beforeTime = System.currentTimeMillis();
            Entity resEntity = HttpUtil.postByJson(requestJson, checkUrl);
            long callTime = System.currentTimeMillis() - beforeTime;
            log.info("调用接口用时: " + callTime + "ms");

            String responseStr = "";
            if (resEntity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
                responseStr = resEntity.getData();
            } else {
                //网络问题，重调查询接口
            }

            log.info("调用查询发表接口返回:" + responseStr);
            InvoiceResponse response = JSON.toJavaObject(JSON.parseObject(responseStr), InvoiceResponse.class);

            //调用失败处理
            if (response.getCode() == InvoiceConstants.FAIL_STATUS) {
                if (response.getMsg().equals(CAPTCHA_ERROR_MSG)) {
                    //验证码错误 需要重新生成验证码
                } else {
                    //判断错误类型，并返回

                }
            }

            return response;

        } else {
            return new InvoiceResponse(InvoiceConstants.NET_WORK_ERROR, "参数不符合规范", null, null);
        }

    }

}

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

    private static final String CAPTCHA_EMPTY_MSG = "验证码不能为空！";

    private static final int maxCycle = 10;

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

        request.setInvoiceType(InvoiceConstants.INVOICE_TYPE);
        request.setCheckCode(InvoiceConstants.DEFAULT_CHECK_CODE);

        return doQueryInvoice(request, 0);
    }

    private InvoiceResponse doQueryInvoice(InvoiceRequest request, int cycleLevel) {

        String checkUrl = invoiceConfig.getCheckUrl();
        Entity entity = captchaService.identifyCaptcha(20);
        if (entity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
            request.setValidCode(entity.getData());
            String requestJson = JSON.toJSONString(request);

            log.info("调用查询接口 入参: " + requestJson);
            long beforeTime = System.currentTimeMillis();
            Entity resEntity;
            try {
                resEntity = HttpUtil.postByJson(requestJson, checkUrl);
            } catch (IOException e) {
                log.error("调用检查发票服务失败", e);
                //TODO recall
                return new InvoiceResponse(InvoiceConstants.NET_WORK_ERROR, "调用检查发票服务失败", null, null);
            }
            long callTime = System.currentTimeMillis() - beforeTime;
            log.info("调用接口用时: " + callTime + "ms");

            String responseStr = resEntity.getData();
            log.info("调用查询发表接口返回:" + responseStr);
            InvoiceResponse response = JSON.toJavaObject(JSON.parseObject(responseStr), InvoiceResponse.class);

            //调用失败处理
            if (response.getCode() == InvoiceConstants.FAIL_STATUS) {
                if (response.getMsg().equals(CAPTCHA_ERROR_MSG)
                        || response.getMsg().equals(CAPTCHA_EMPTY_MSG)) {
                    //验证码错误或为空，需要重新生成验证码
                    if (cycleLevel < maxCycle) {
                        return doQueryInvoice(request, cycleLevel + 1);
                    }
                } else {
                    //其余失败类型返回
                    response.setMsg("未查到发票信息");
                    return response;
                }
            }
            //调用成功
            return response;
        } else {
            //验证码服务调用失败
            log.warn("无法获取有效的验证码");
            return new InvoiceResponse(InvoiceConstants.FAIL_STATUS, "验证码服务调用失败", null, null);
        }
    }

}

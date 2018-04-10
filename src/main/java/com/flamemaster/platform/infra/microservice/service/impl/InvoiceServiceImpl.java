package com.flamemaster.platform.infra.microservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.flamemaster.platform.infra.microservice.base.Entity;
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

import javax.annotation.Resource;
import java.io.IOException;

@Log4j
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Resource
    private InvoiceConfig invoiceConfig;

    @Resource
    private CaptchaService captchaService;

    /**
     * 查询发票信息
     * @param request 查询参数
     * @return 发票信息
     */
    public InvoiceResponse queryInvoice(InvoiceRequest request) {

        //TODO 参数检查

        String checkUrl = invoiceConfig.getCheckUrl();
        Entity entity = captchaService.identifyCaptcha();
        if (entity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
            request.setValidCode(entity.getData());
            try {
                String requestJson = JSON.toJSONString(request);
                String responseStr = HttpUtil.postByJson(requestJson, checkUrl);
                log.info("调用查询发表接口返回:" + responseStr);
                InvoiceResponse responseData = JSON.toJavaObject(JSON.parseObject(responseStr), InvoiceResponse.class);
                return responseData;
            } catch (IOException e) {
                log.error("调用查询发票接口失败", e);
            }
        } else {
            //TODO recall
        }
        return null;
    }

}

package com.flamemaster.platform.infra.microservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.flamemaster.platform.infra.microservice.base.Entity;
import com.flamemaster.platform.infra.microservice.base.InvoiceData;
import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceResponse;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.config.InvoiceConstants;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import com.flamemaster.platform.infra.microservice.util.HttpUtil;
import lombok.extern.log4j.Log4j;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
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
    public InvoiceData queryInvoice(InvoiceRequest request) {

        //TODO 参数检查

        String checkUrl = invoiceConfig.getCheckUrl();
        Entity entity = captchaService.getCaptcha();
        if (entity.getCode() == InvoiceConstants.SUCCESS_STATUS) {
            String data = entity.getData();
            log.info(data);
            Entity identifyResult = captchaService
                    .identifyCaptcha(Base64Utils.encodeToString(data.getBytes()));
            if (identifyResult.getCode() == InvoiceConstants.SUCCESS_STATUS) {
                request.setCheckCode(identifyResult.getData());
                try {
                    String responseStr = HttpUtil.postByJson(JSON.toJSONString(request), checkUrl);
                    log.info("调用查询发表接口返回:" + responseStr);
                    InvoiceData responseData = JSON.toJavaObject(JSON.parseObject(responseStr), InvoiceData.class);
                    return responseData;
                } catch (IOException e) {
                    log.error("调用查询发票接口失败", e);
                }
            } else {
                //TODO recall
            }
        } else {
            //TODO recall
        }
        return null;
    }

}

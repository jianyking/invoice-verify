package com.flamemaster.platform.infra.microservice.service.impl;

import com.flamemaster.platform.infra.microservice.base.InvoiceData;
import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.config.InvoiceConfig;
import com.flamemaster.platform.infra.microservice.service.CaptchaService;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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

        String checkUrl = invoiceConfig.getCheckUrl();
        return null;
    }

}

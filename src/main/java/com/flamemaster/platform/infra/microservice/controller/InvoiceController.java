package com.flamemaster.platform.infra.microservice.controller;

import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceResponse;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
public class InvoiceController {

    @Resource
    private InvoiceService invoiceService;

    /**
     * 查验发票接口
     * @param request 发票参数
     * @return 查验结果
     */
    @RequestMapping("/checkInvoice")
    @ResponseBody
    InvoiceResponse checkInvoice(@RequestBody InvoiceRequest request) {
        return invoiceService.queryInvoice(request);
    }
}

package com.flamemaster.platform.infra.microservice.controller;

import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceResponse;
import com.flamemaster.platform.infra.microservice.service.InvoiceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(value = "invoice", description = "发票验真服务")
@RestController
public class InvoiceController {

    @Resource
    private InvoiceService invoiceService;

    /**
     * 查验发票接口
     * @param request 发票参数
     * @return 查验结果
     */
    @ApiOperation(value = "checkInvoice", notes = "发票验真接口", response = InvoiceResponse.class)
    @RequestMapping(method = RequestMethod.POST, path = "/api/checkInvoice", produces = "application/json", consumes = "application/json")
    @ResponseBody
    InvoiceResponse checkInvoice(@RequestBody InvoiceRequest request) {
        return invoiceService.queryInvoice(request);
    }
}

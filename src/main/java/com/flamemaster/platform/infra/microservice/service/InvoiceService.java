package com.flamemaster.platform.infra.microservice.service;

import com.flamemaster.platform.infra.microservice.base.InnerInvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceRequest;
import com.flamemaster.platform.infra.microservice.base.InvoiceResponse;

public interface InvoiceService {

    /**
     * 查询发票信息
     * @param request 查询参数
     * @return 发票信息
     */
    InvoiceResponse queryInvoice(InvoiceRequest request);

}

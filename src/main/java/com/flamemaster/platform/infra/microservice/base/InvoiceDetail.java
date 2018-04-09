package com.flamemaster.platform.infra.microservice.base;

import lombok.Data;

@Data
public class InvoiceDetail {

    private String detailNo;

    private String goodsName;

    private String detailAmount;

    private String num;

    private String taxRate;

    private String taxAmount;

    private String taxUnitPrice;

    private String taxDetailAmount;

    private String unitPrice;

    private String specificationModel;

    private String unit;

}

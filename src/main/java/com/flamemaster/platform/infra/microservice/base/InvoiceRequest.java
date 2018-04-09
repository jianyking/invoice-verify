package com.flamemaster.platform.infra.microservice.base;

import lombok.Data;

@Data
public class InvoiceRequest {

    private String invoiceType;

    private String invoiceCode;

    private String invoiceNo;

    private String invoiceDate;

    private String invoiceAmount;

    private String checkCode;

    private String validCode;

}

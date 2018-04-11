package com.flamemaster.platform.infra.microservice.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InnerInvoiceRequest {

    private String invoiceType;

    private String invoiceCode;

    private String invoiceNo;

    private String invoiceDate;

    private String invoiceAmount;

    private String checkCode;

    private String validCode;

}

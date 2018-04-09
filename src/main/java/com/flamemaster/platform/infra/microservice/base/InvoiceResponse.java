package com.flamemaster.platform.infra.microservice.base;

import lombok.Data;

@Data
public class InvoiceResponse {

    private int code;

    private String msg;

    private InvoiceData data;

    private String errorCode;
}

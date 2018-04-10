package com.flamemaster.platform.infra.microservice.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {

    private int code;

    private String msg;

    private InvoiceData data;

    private String errorCode;
}

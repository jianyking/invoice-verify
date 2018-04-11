package com.flamemaster.platform.infra.microservice.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InvoiceRequest {

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;

    @ApiModelProperty(value = "发票金额")
    private String invoiceAmount;

}


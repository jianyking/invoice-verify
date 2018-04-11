package com.flamemaster.platform.infra.microservice.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class InvoiceResponse {

    @ApiModelProperty(value = "响应码")
    private int code;

    @ApiModelProperty(value = "错误信息")
    private String msg;

    @ApiModelProperty(value = "发票数据")
    private InvoiceData data;

    @ApiModelProperty(value = "错误码")
    private String errorCode;
}

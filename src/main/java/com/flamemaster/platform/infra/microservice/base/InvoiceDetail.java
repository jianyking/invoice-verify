package com.flamemaster.platform.infra.microservice.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class InvoiceDetail {

    private String detailNo;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "商品总价")
    private String detailAmount;

    @ApiModelProperty(value = "数量")
    private String num;

    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "总税额")
    private String taxAmount;

    private String taxUnitPrice;

    private String taxDetailAmount;

    @ApiModelProperty(value = "单个商品价格")
    private String unitPrice;

    private String specificationModel;

    private String unit;

}

package com.flamemaster.platform.infra.microservice.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class InvoiceData {

    @ApiModelProperty(value = "响应码")
    private String resultCode;

    @ApiModelProperty(value = "查验结果描述")
    private String resultTip;

    @ApiModelProperty(value = "发票类型")
    private String invoiceType;

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo;

    private String checkCount;

    @ApiModelProperty(value = "销售方纳税人识别号")
    private String salerTaxNo;

    @ApiModelProperty(value = "销售方名称")
    private String salerName;

    @ApiModelProperty(value = "销售方地址电话")
    private String salerAddressPhone;

    @ApiModelProperty(value = "销售方开户行银行号")
    private String salerAccount;

    @ApiModelProperty(value = "购买方纳税人识别号")
    private String buyerTaxNo;

    @ApiModelProperty(value = "购买方名称")
    private String buyerName;

    @ApiModelProperty(value = "购买方地址电话")
    private String buyerAddressPhone;

    @ApiModelProperty(value = "购买方开户行银行号")
    private String buyerAccount;

    @ApiModelProperty(value = "开票日期")
    private String invoiceDate;

    @ApiModelProperty(value = "金额")
    private String invoiceAmount;

    @ApiModelProperty(value = "税额")
    private String taxAmount;

    @ApiModelProperty(value = "价税合计")
    private String totalAmount;

    private String remark;

    @ApiModelProperty(value = "机器编号")
    private String machineNo;

    private String drawer;

    private String payee;

    private String reviewer;

    @ApiModelProperty(value = "校验码")
    private String checkCode;

    private String blueInvoiceCode;

    private String blueInvoiceNo;

    private String cancellationMark;

    @ApiModelProperty(value = "商品详情")
    private List<InvoiceDetail> detailList;

}

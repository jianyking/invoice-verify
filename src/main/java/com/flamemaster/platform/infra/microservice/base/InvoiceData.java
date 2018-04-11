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

    private String resultTip;

    private String invoiceType;

    private String invoiceCode;

    private String invoiceNo;

    private String checkCount;

    private String salerTaxNo;

    private String salerName;

    private String salerAddressPhone;

    private String salerAccount;

    private String buyerTaxNo;

    private String buyerName;

    private String buyerAddressPhone;

    private String buyerAccount;

    private String invoiceDate;

    private String invoiceAmount;

    private String taxAmount;

    private String totalAmount;

    private String remark;

    private String machineNo;

    private String drawer;

    private String payee;

    private String reviewer;

    private String checkCode;

    private String blueInvoiceCode;

    private String blueInvoiceNo;

    private String cancellationMark;

    private List<InvoiceDetail> detailList;

}

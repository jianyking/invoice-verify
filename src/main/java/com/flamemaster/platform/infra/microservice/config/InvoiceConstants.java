package com.flamemaster.platform.infra.microservice.config;

public class InvoiceConstants {

    public final static int SUCCESS_STATUS = 0;

    /** 通用错误类型 **/
    public final static int FAIL_STATUS = 1;

    /** 参数不符合规范 **/
    public final static int PARAM_NOT_VALID = 2;

    /** 网络出错 **/
    public final static int NET_WORK_ERROR = 3;

    /** 服务调用失败 **/
    public final static int SERVICE_ERROR = 4;

    /** 默认的查询发票接口所需查询类型 **/
    public final static String INVOICE_TYPE = "01";
}

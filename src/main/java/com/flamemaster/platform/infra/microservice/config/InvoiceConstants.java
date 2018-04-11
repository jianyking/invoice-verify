package com.flamemaster.platform.infra.microservice.config;

import com.flamemaster.platform.infra.microservice.base.InvoiceData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvoiceConstants {

    public final static int SUCCESS_STATUS = 0;

    /** 通用错误 **/
    public final static int FAIL_STATUS = 1;

    /** 参数不符合规范 **/
    public final static int PARAM_NOT_VALID = 2;

    /** 未查到发票信息 **/
    public final static int INVOICE_ERROR = 3;

    /** 网络出错 **/
    public final static int NET_WORK_ERROR = 4;

    /** 验证码服务出错 **/
    public final static int CAPTCHA_CODE_ERROR = 5;

    /** 默认的查询发票接口所需查询类型 **/
    public final static String INVOICE_TYPE = "01";

    public final static String DEFAULT_CHECK_CODE = "";

    public static final String TEMP_IMAGE_FORMAT = "bmp";

    public static final String TESSERACT_OCR_LANGUAGE = "eng";

    public static final String RUN_MODE_DEBUG = "debug";

    public static final String RUN_MODE_NORMAL = "normal";

    public static final Long CAPTCHA_CACHE_FAILURE_TIME = 120L * 1000L;

    public static final String CHECK_INVOICE_CODE_OK = "0001";

    public static final String CHECK_INVOICE_CODE_ERROR = "0006";
}

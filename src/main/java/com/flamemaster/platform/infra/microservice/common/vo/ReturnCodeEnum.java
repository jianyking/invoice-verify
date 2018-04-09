package com.flamemaster.platform.infra.microservice.common.vo;

import java.util.HashSet;

/**
 * 类ReturnCodeEnum.java的实现描述：返回异常码
 */
public enum ReturnCodeEnum {

    /**
     * 成功
     */
    RESTFUL_SUCCESS(0, "成功"),
    /**
     * 处理失败，未知异常
     */
    TEMPLATE_ID_ERROR(-1, "模板id错误"),
    RESTFUL_FAIL(500, "处理失败，未知异常"),
    RESTFUL_REQUEST_OBJECT_INVALID(400, "参数验证失败"),
    COMMON(999, "未知业务异常"),
    PRE_MAPPING_ID_UNDEFINED(1001, "在中间表数据SQL表中未找到这条数据"),
    APP_NAME_EXIST(1002, "应用名称已存在"),
    MID_DATA_TABLE_UNDEFINED(2001, "中间表不存在"),
    MID_DATA_TABLE_SQL_ERROR(2002, "中间表SQL执行错误"),
    APP_NOT_EXIST(2003, "应用不存在"),
    SECTION_COL_IS_NOT_NUMBER(4001, "该字段为非数字型，无法自动分段"),
    APP_SECTION_NOT_EXIST(4002, "该应用分段数据不存在"),
    CHART_SQL_CREATE_ERROR(5001, "信息缺失，生成SQL失败"),
    CHART_SQL_IS_NULL(6001, "抽取SQL为空"),
    CHART_SQL_RUN_ERROR(6002, "抽取SQL执行失败"),
    CHART_SAVE_ERROR(7001, "保存图表失败"),
    CHART_IMAGE_ERROR(7002, "图像转换异常"),
    UNKNOWN_FAIL(10000, "处理失败[未知原因]");;


    private static HashSet<Integer> hashSet;

    static {
        hashSet = new HashSet<Integer>();
        hashSet.clear();
        for (ReturnCodeEnum returnStatus : ReturnCodeEnum.values()) {
            hashSet.add(returnStatus.value());
        }
    }

    private final int value;
    /**
     * The desc.
     */
    private final String desc;

    /**
     * Instantiates a new return status enum.
     *
     * @param value
     * @param desc  the desc
     */
    private ReturnCodeEnum(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static boolean isDefined(int value) {
        if (hashSet.contains(value)) {
            return true;
        }
        return false;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int value() {
        return value;
    }

    /**
     * Gets the desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}

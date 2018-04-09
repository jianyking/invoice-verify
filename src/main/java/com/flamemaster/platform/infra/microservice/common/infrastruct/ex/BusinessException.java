package com.flamemaster.platform.infra.microservice.common.infrastruct.ex;


import com.flamemaster.platform.infra.microservice.common.vo.ReturnCodeEnum;

/**
 */
public class BusinessException extends RuntimeException {
    /**
     * 序列化id
     */
    private static final long serialVersionUID = -5147581356988161519L;
    /**
     * 错误码
     */
    private ReturnCodeEnum returnCodeEnum;

    private Exception exception;
    /**
     * 错误信息
     */
    private String message;


    public BusinessException(ReturnCodeEnum returnCodeEnum, Exception e) {
        super();
        this.returnCodeEnum = returnCodeEnum;
        this.exception = e;
    }

    public BusinessException(ReturnCodeEnum returnCodeEnum, String message, Exception e) {
        super(message);
        this.returnCodeEnum = returnCodeEnum;
        this.message = message;
        this.exception = e;
    }

    public BusinessException(ReturnCodeEnum returnCodeEnum, String message) {
        super(message);
        this.returnCodeEnum = returnCodeEnum;
        this.message = message;
    }


    public BusinessException(ReturnCodeEnum returnCodeEnum) {
        super(returnCodeEnum.getDesc());
        this.returnCodeEnum = returnCodeEnum;
        // this.message = ReturnCodeEnum.getDesc();
    }

    public BusinessException(String message) {
        super(message);
        this.returnCodeEnum = ReturnCodeEnum.COMMON;
        this.message = message;
    }

    public ReturnCodeEnum getReturnCodeEnum() {
        return returnCodeEnum;
    }

    public void setReturnCodeEnum(ReturnCodeEnum returnCodeEnum) {
        this.returnCodeEnum = returnCodeEnum;
    }

    @Override
    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }
}

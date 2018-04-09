package com.flamemaster.platform.infra.microservice.common.vo;

/**
 * Created by jingxu on 14/02/2017.
 */
public interface ResultI {
    Integer getCode();

    void setCode(Integer code);

    String getMsg();

    void setMsg(String msg);
}

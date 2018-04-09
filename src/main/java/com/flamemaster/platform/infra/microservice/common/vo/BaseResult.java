package com.flamemaster.platform.infra.microservice.common.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@ApiModel
public class BaseResult implements java.io.Serializable, ResultI {
    private Integer code = ReturnCodeEnum.RESTFUL_SUCCESS.value();
    private String msg = ReturnCodeEnum.RESTFUL_SUCCESS.getDesc();

    @ApiModelProperty("返回code, 0成功, 非0失败")
    @Override
    public Integer getCode() {
        return code;
    }
    @Override
    public void setCode(Integer code) {
        this.code = code;
    }
    @Override
    public String getMsg() {
        return msg;
    }
    @Override
    public void setMsg(String msg) {
        this.msg = msg;
    }
}

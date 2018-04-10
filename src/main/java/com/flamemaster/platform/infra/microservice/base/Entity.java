package com.flamemaster.platform.infra.microservice.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entity {

    private int code;

    private String msg;

    private String data;

}

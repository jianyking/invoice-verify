/**
 * Created by Administrator on 2016/12/17.
 */
@GenericGenerator(
        name = "ID_GEN",
        strategy = "enhanced-sequence",
        parameters = {
                @Parameter(name = "sequence_name", value = "ID_GEN"),
                @Parameter(name = "initial_value", value = "10000000"),
                @Parameter(name = "increment_size", value = "100"),
                @Parameter(name = "optimizer", value = "pooled-lo")
        }
)
package com.flamemaster.platform.infra.microservice.common.domain.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
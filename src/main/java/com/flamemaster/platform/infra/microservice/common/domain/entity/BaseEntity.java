package com.flamemaster.platform.infra.microservice.common.domain.entity;

import lombok.Data;
import org.joda.time.DateTime;

import javax.persistence.*;

@MappedSuperclass
@Data
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected DateTime createTime;

    protected DateTime updateTime;

    @PrePersist
    private void createTime() {
        this.createTime = new DateTime();
        this.updateTime = this.createTime;
    }

    @PreUpdate
    private void updateTime() {
        this.updateTime = new DateTime();
    }
}

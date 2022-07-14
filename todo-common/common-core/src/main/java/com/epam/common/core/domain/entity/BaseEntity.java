package com.epam.common.core.domain.entity;

public class BaseEntity {

    /**
     * 每个实体一个ID
     */
    private Long id;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return this.id;
    }
}

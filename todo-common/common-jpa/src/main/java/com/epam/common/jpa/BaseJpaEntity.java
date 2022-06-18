package com.epam.common.jpa;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import java.time.LocalDateTime;

@Data
@EntityListeners(value = AuditingEntityListener.class)
public class BaseJpaEntity {

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedTime;
}

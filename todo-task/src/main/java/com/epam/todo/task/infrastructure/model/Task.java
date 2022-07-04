package com.epam.todo.task.infrastructure.model;

import com.epam.todo.common.jpa.BaseJpaEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "task")
public class Task extends BaseJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "sub_title", nullable = false)
    private String subTitle;

    @Column(name = "deleted", nullable = false)
    private Integer deleted;

    @Column(name = "status", nullable = false)
    private Integer status;
}
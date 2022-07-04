package com.epam.todo.usercenter.domain.entity;

import lombok.Data;

@Data
public class UserEntity {

    private String username;

    private String password;

    private Long talentId;
}

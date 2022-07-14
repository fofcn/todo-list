package com.epam.todo.auth.infrastructure.auth;

import lombok.Data;

@Data
public class UserDO {

    private Long userId;

    private String username;

    private Long talentId;
}

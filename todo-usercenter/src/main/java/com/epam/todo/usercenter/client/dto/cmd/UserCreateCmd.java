package com.epam.todo.usercenter.client.dto.cmd;

import lombok.Data;

@Data
public class UserCreateCmd {

    private String username;

    private String password;

    private Long talentId;
}

package com.epam.todo.auth.client.dto.cmd;

import lombok.Data;

@Data
public class AuthRefreshTokenCmd {

    private String refreshToken;

}

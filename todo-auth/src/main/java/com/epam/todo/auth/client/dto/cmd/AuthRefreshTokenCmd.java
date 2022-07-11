package com.epam.todo.auth.client.dto.cmd;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AuthRefreshTokenCmd {

    @NotNull(message = "refreshToken can not be null")
    @NotEmpty(message = "refreshToken can not be empty")
    private String refreshToken;

}

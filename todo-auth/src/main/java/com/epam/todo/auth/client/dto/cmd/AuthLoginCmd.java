package com.epam.todo.auth.client.dto.cmd;

import lombok.Data;

import java.security.Principal;

@Data
public class AuthLoginCmd {

    /**
     * username
     */
    private String username;

    /**
     * password
     */
    private String password;

    /**
     * remember me
     */
    private boolean rememberMe;
}

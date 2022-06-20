package com.epam.todo.auth.client.dto.cmd;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AuthLoginCmd {

    /**
     * username
     */
    @NotNull(message = "Username can not be null")
    @NotEmpty(message = "Username can not be empty")
    @Length(min = 3, max = 60, message = "The length of username is too short or too long")
    @Email
    private String username;

    /**
     * password
     */
    @NotNull(message = "password can not be null")
    @NotEmpty(message = "password can not be empty")
    @Length(min = 8, max = 20, message = "The length of password is too short or too long")
    @Pattern(regexp = "[A-z\\d]{8,20}", message = "password does not follow the password rules ")
    private String password;

    /**
     * remember me
     */
    @NotNull(message = "Remember me should not be null")
    private boolean rememberMe;
}

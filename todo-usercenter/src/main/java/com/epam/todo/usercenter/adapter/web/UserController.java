package com.epam.todo.usercenter.adapter.web;

import com.epam.common.core.dto.Response;
import com.epam.todo.usercenter.client.api.UserService;
import com.epam.todo.usercenter.client.dto.cmd.UserCreateCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Response createUser(UserCreateCmd cmd) {
        return userService.createUser(cmd);
    }
}

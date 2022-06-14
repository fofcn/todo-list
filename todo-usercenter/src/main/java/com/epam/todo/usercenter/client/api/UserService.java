package com.epam.todo.usercenter.client.api;

import com.epam.common.core.dto.Response;
import com.epam.todo.usercenter.client.dto.cmd.UserCreateCmd;

public interface UserService {

    Response createUser(UserCreateCmd cmd);
}

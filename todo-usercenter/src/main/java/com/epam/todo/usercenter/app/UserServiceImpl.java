package com.epam.todo.usercenter.app;

import com.epam.common.core.dto.Response;
import com.epam.todo.usercenter.app.executor.UserCreateCmdExe;
import com.epam.todo.usercenter.client.api.UserService;
import com.epam.todo.usercenter.client.dto.cmd.UserCreateCmd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserCreateCmdExe userCreateCmdExe;

    @Override
    public Response createUser(UserCreateCmd cmd) {
        return userCreateCmdExe.execute(cmd);
    }
}

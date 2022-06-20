package com.epam.todo.usercenter.app.executor;

import com.epam.common.core.ResponseCode;
import com.epam.common.core.dto.Response;
import com.epam.common.core.exception.TodoException;
import com.epam.todo.usercenter.client.dto.cmd.UserCreateCmd;
import com.epam.todo.usercenter.infrastructure.model.User;
import com.epam.todo.usercenter.infrastructure.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCreateCmdExe {

    @Autowired
    private UserRepository userRepository;

    public Response execute(UserCreateCmd cmd) {
        User exists = userRepository.findByUsername(cmd.getUsername());
        if (exists != null) {
            // 这里可以用assert
            throw new TodoException(ResponseCode.USER_EXISTS);
        }
        // cmd 转 model
        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        user.setLastModifiedTime(LocalDateTime.now());
        user.setUsername(cmd.getUsername());
        user.setPassword(PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(cmd.getPassword()));
        user.setTalentId(cmd.getTalentId());
        userRepository.save(user);
        return Response.buildSuccess();
    }
}

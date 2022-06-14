package com.epam.todo.usercenter.app.executor;

import com.epam.common.core.dto.Response;
import com.epam.todo.usercenter.client.dto.cmd.UserCreateCmd;
import com.epam.todo.usercenter.infrastructure.model.User;
import com.epam.todo.usercenter.infrastructure.repository.UserRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserCreateCmdExe {

    @Auto
    private UserRepository userRepository;

    public Response execute(UserCreateCmd cmd) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // cmd 转 model
        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        user.setLastModifiedTime(LocalDateTime.now());
        user.setUsername(cmd.getUsername());
        user.setPassword(passwordEncoder.encode(cmd.getPassword()));
        user.setTalentId(cmd.getTalentId());
        userRepository.save(user);
        return Response.buildSuccess();
    }
}

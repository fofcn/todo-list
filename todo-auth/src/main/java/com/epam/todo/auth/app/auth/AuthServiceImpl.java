package com.epam.todo.auth.app.auth;

import com.epam.common.core.dto.SingleResponse;
import com.epam.fans.auth.client.api.AuthService;
import com.epam.fans.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.fans.auth.client.dto.data.AuthTokenDTO;
import com.epam.todo.auth.app.auth.executor.AuthLoginCmdExe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthLoginCmdExe authLoginCmdExe;

    @Override
    public SingleResponse<AuthTokenDTO> token(AuthLoginCmd cmd) {
        return authLoginCmdExe.execute(cmd);
    }
}

package com.epam.todo.auth.app.auth;

import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.auth.app.auth.executor.AuthLoginCmdExe;
import com.epam.todo.auth.app.auth.executor.AuthRefreshTokenCmdExec;
import com.epam.todo.auth.client.api.AuthService;
import com.epam.todo.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.todo.auth.client.dto.cmd.AuthRefreshTokenCmd;
import com.epam.todo.auth.client.dto.data.AuthRefreshTokenDTO;
import com.epam.todo.auth.client.dto.data.AuthTokenDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthLoginCmdExe authLoginCmdExe;

    @Autowired
    private AuthRefreshTokenCmdExec authRefreshTokenCmdExec;

    @Override
    public SingleResponse<AuthTokenDTO> token(AuthLoginCmd cmd) {
        return authLoginCmdExe.execute(cmd);
    }

    @Override
    public SingleResponse<AuthRefreshTokenDTO> refreshToken(AuthRefreshTokenCmd cmd) {
        return authRefreshTokenCmdExec.execute(cmd);
    }
}

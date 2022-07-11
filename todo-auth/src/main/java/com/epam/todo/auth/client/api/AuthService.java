package com.epam.todo.auth.client.api;

import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.todo.auth.client.dto.cmd.AuthRefreshTokenCmd;
import com.epam.todo.auth.client.dto.data.AuthRefreshTokenDTO;
import com.epam.todo.auth.client.dto.data.AuthTokenDTO;

public interface AuthService {

    SingleResponse<AuthTokenDTO> token(AuthLoginCmd cmd);

    SingleResponse<AuthRefreshTokenDTO> refreshToken(AuthRefreshTokenCmd cmd);
}

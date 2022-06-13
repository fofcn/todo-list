package com.epam.todo.auth.client.api;

import com.epam.common.core.dto.SingleResponse;
import com.epam.fans.auth.client.dto.data.AuthTokenDTO;
import com.epam.todo.auth.client.dto.cmd.AuthLoginCmd;

public interface AuthService {

    SingleResponse<AuthTokenDTO> token(AuthLoginCmd cmd);
}

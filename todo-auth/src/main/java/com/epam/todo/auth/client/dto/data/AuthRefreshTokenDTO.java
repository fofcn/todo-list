package com.epam.todo.auth.client.dto.data;

import lombok.Data;

@Data
public class AuthRefreshTokenDTO {
    /**
     * access token
     */
    private String accessToken;

    /**
     * refresh token
     */
    private String refreshToken;

    /**
     * type of this token
     */
    private String type;

    /**
     * expire time, time unit: seconds
     */
    private int expireIn;
}

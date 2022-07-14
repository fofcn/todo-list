package com.epam.todo.auth.adapter.web;

import com.alibaba.fastjson.JSONObject;
import com.epam.common.core.constant.SecurityConstants;
import com.epam.common.core.dto.Response;
import com.epam.common.core.dto.SingleResponse;
import com.epam.todo.auth.client.api.AuthService;
import com.epam.todo.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.todo.auth.client.dto.cmd.AuthRefreshTokenCmd;
import com.epam.todo.auth.client.dto.data.AuthRefreshTokenDTO;
import com.epam.todo.auth.client.dto.data.AuthTokenDTO;
import com.epam.todo.common.web.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@RestController
public class LoginController {

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/token")
    public SingleResponse<AuthTokenDTO> token(@RequestBody @Valid AuthLoginCmd cmd) {
        return authService.token(cmd);
    }

    @PostMapping("/refreshToken")
    public SingleResponse<AuthRefreshTokenDTO> refreshToken(@RequestBody @Valid AuthRefreshTokenCmd cmd) {
        return authService.refreshToken(cmd);
    }

    @DeleteMapping("/token")
    public Response logout() {
        JSONObject payload = JwtUtils.getJwtPayload();
        String jti = payload.getString(SecurityConstants.JWT_JTI); // JWT唯一标识
        Long expireTime = payload.getLong(SecurityConstants.JWT_EXP); // JWT过期时间戳(单位：秒)
        if (expireTime != null) {
            long currentTime = System.currentTimeMillis() / 1000;// 当前时间（单位：秒）
            if (expireTime > currentTime) { // token未过期，添加至缓存作为黑名单限制访问，缓存时间为token过期剩余时间
                redisTemplate.opsForValue().set(SecurityConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (expireTime - currentTime), TimeUnit.SECONDS);
            }
        } else { // token 永不过期则永久加入黑名单
            redisTemplate.opsForValue().set(SecurityConstants.TOKEN_BLACKLIST_PREFIX + jti, null);
        }
        return Response.buildSuccess();
    }

}


package com.epam.todo.auth.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.epam.cloud.common.core.constant.SecurityConstants;
import com.epam.cloud.common.core.dto.Response;
import com.epam.cloud.common.core.dto.SingleResponse;
import com.epam.cloud.common.web.util.JwtUtils;
import com.epam.fans.auth.client.api.AuthService;
import com.epam.fans.auth.client.dto.cmd.AuthLoginCmd;
import com.epam.fans.auth.client.dto.data.AuthTokenDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public SingleResponse<AuthTokenDTO> token(@RequestBody AuthLoginCmd cmd) {
        return authService.token(cmd);
    }

    @PostMapping("/refreshToken")
    public SingleResponse<AuthTokenDTO> refreshToken(@RequestBody AuthLoginCmd cmd) {
        JSONObject jsonObject = JwtUtils.getJwtPayload();
        log.info("jwt payload: {}, username: ", JSON.toJSONString(jsonObject), jsonObject.getString("user_name"));
        return SingleResponse.of(null);
    }

    @DeleteMapping("/logout")
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


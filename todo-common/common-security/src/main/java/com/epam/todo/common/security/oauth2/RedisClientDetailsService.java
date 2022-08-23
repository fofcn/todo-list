package com.epam.todo.common.security.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

/**
 * Client details based on redis storage.
 * @author Ford_Ji
 */
@Component
@Configuration
public class RedisClientDetailsService implements ClientDetailsService {

    @Autowired
    private RedisTemplate<String, ClientDetails> redisTemplate;

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return redisTemplate.opsForValue().get(clientId);
    }
}

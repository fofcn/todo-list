package com.epam.todo.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

@ConditionalOnProperty(prefix = "redisson",name = "address")
@ConfigurationProperties(prefix = "redisson")
@Configuration
public class RedissonConfig {

    private String address;
    private String password;
    private Integer database;
    private Integer minIdle;// 默认最小空闲连接数

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setDatabase(database);
        singleServerConfig.setConnectionMinimumIdleSize(minIdle);
        if (StringUtils.hasLength(password)) {
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }
}

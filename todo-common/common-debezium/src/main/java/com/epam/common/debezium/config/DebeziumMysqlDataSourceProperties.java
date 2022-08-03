package com.epam.common.debezium.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "debezium.datasource")
public class DebeziumMysqlDataSourceProperties {

    /**
     * Database details.
     */
    private String host;

    private String database;

    private String port;

    private String username;

    private String password;

}

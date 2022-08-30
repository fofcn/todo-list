package com.epam.common.debezium.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "debezium.datasource")
public class DebeziumMysqlDataSourceProperties {

    /**
     * Database host.
     */
    private String host;

    /**
     * database port
     */
    private String port;

    /**
     * database username
     */
    private String username;

    /**
     * database password
     */
    private String password;

    /**
     * database list include
     */
    private String databaseIncludeList = "";

    /**
     * database list exclude
     */
    private String databaseExcludeList = "";

    /**
     * table list included
     * either tableIncludeList or tableExcludeList can be specified not both
     */
    private String tableIncludeList = "";

    /**
     * table list excluded
     * either tableIncludeList or tableExcludeList can be specified not both
     */
    private String tableExcludeList = "";

    /**
     * column list included
     */
    private String columnIncludeList = "";

    /**
     * column list excluded
     * either columnIncludeList or columnExcludeList can be specified not both
     */
    private String columnExcludeList = "";
}

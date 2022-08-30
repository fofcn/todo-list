package com.epam.common.debezium;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

//@Configuration
public class MySQLTestContainerConfiguration {

    public static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");

    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>(MYSQL_IMAGE)
            .withCommand("--default-authentication-plugin=mysql_native_password")
            .withInitScript("mysql/user.sql")
            .withDatabaseName("test_source_db")
            .withUsername("user")
            .withPassword("user")
            .withEnv("MYSQL_ROOT_PASSWORD", "user")
            .withExtraHost("test_mysql", "127.0.0.1");

    MySQLTestContainerConfiguration() {
        mysqlContainer.start();
    }

    public static int getPort() {
        return mysqlContainer.getFirstMappedPort();
    }

    @Bean
    @Primary
    public EmbeddedDatabase targetDatasource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .setName("test_target_db")
                .build();
    }

    @Bean(name = "SOURCE_DS")
    public DataSource sourceDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(mysqlContainer.getJdbcUrl());
        dataSource.setUsername(mysqlContainer.getUsername());
        dataSource.setPassword(mysqlContainer.getPassword());
        return dataSource;
    }

    @Bean(name = "sourceJdbcTemplate")
    public NamedParameterJdbcTemplate getJdbcTemplate(@Qualifier("SOURCE_DS") DataSource sourceDataSource) {
        return new NamedParameterJdbcTemplate(sourceDataSource);
    }
}

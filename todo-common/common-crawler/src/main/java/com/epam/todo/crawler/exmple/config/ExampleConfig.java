package com.epam.todo.crawler.exmple.config;

import lombok.Data;

import java.util.List;

@Data
public class ExampleConfig {

    /**
     * domain
     */
    private String domain;

    /**
     * host store path
     */
    private String hostStorePath;

    /**
     * seeds
     */
    private String seeds;

    /**
     * allowed domains
     */
    private List<String> allowedDomains;
}

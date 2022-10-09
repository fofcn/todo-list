package com.epam.common.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@OpenAPIDefinition
@Configuration
public class SpringDocConfig {
    
    @Bean
    public OpenAPI heroOpenApi() {
        return new OpenAPI()
                .info(info())
                .externalDocs(new ExternalDocumentation().description("").url(""));
    }

    private Info info() {
        return new Info()
                .title("Task Manager")
                .description("Task Manager Api Document")
                .version("v1.0.0");
    }
}

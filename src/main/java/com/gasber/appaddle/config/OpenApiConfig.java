package com.gasber.appaddle.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    
    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
                .group("appaddle") // ⚠️ Este valor NO puede estar vacío
                .pathsToMatch("/**")
                .build();
    }
    
}

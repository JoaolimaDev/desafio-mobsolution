package com.mobsolution.spring_app.config.security.utils;


import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.models.security.SecurityRequirement;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class OpenApiConfig {

    @Bean
    public OpenApiCustomizer repositorySecurityCustomiser() {
        return openApi -> openApi.getPaths().forEach((path, pathItem) ->
            pathItem.readOperations().forEach(operation ->
                operation.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            )
        );
    }
}

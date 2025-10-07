package com.example.todo_app.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.security")
@Getter
@Setter
public class AllowedOrigins {
    private String allowedOrigins;
}
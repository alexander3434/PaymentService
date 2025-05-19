package io.murkka34.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//@Component
public class DatabaseConfig {
    @Value("${spring.datasource.url}")
    private String dbUrl;
}
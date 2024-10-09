package com.example.GoAdventure.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AdminConfig {
    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.email}")
    private String email;

    @Value("${admin.phone}")
    private String phone;
}
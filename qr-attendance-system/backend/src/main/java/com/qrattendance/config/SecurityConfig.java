package com.qrattendance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for local development APIs
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permit all for easier initial development
            )
            .headers(headers -> headers.frameOptions(frame -> frame.disable())); // Allow H2 console
        
        return http.build();
    }
}

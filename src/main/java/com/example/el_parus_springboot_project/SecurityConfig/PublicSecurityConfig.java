package com.example.el_parus_springboot_project.SecurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(2)
public class PublicSecurityConfig {


        @Bean
        public SecurityFilterChain publicSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .securityMatcher("/cart/**")
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authz -> authz
                            .requestMatchers("/cart/**").permitAll()
                            .anyRequest().denyAll()
                    );
            return http.build();
        }
    }




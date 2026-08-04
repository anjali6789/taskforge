package com.taskforge.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // REST APIs don't use browser form submissions, so CSRF is not needed
            .csrf(csrf -> csrf.disable())

            // Define which URLs require authentication
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/**").permitAll()   // open for now
                .anyRequest().authenticated()                // everything else locked
            )

            // No HTML login page needed — this is a JSON API
            .formLogin(form -> form.disable());

        return http.build();
    }
}

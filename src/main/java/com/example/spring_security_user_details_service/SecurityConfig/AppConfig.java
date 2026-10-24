package com.example.spring_security_user_details_service.SecurityConfig;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class AppConfig {

    // 1. PasswordEncoder ki Bean yahan banayein (Inject mat karein)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // REST APIs ke liye CSRF disable
                .csrf(csrf -> csrf.disable())

                // Endpoints authorize karein
                .authorizeHttpRequests(auth -> auth
                        // Registration URL yahan match hona zaroori hai
                        .requestMatchers("/api/register/**", "/register/**").permitAll()
                        // Baaki sab private
                        .anyRequest().authenticated()
                )

                // Postman basic testing ke liye
                .httpBasic(Customizer.withDefaults())

                // Stateless session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        return http.build();
    }
}
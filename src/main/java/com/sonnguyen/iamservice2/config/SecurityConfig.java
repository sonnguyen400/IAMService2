package com.sonnguyen.iamservice2.config;

import com.sonnguyen.iamservice2.security.JwtFilter;
import com.sonnguyen.iamservice2.security.LockAccountFilter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    JwtFilter jwtFilter;
    LockAccountFilter lockAccountFilter;

    @Bean
    public SecurityFilterChain configSecurityWithExternalIdp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated();
        });
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(lockAccountFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

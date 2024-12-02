package com.sonnguyen.iamservice2.config;

import com.sonnguyen.iamservice2.security.JwtFilter;
import com.sonnguyen.iamservice2.security.Oauth2SuccessHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
    Oauth2SuccessHandler oauth2SuccessHandler;
    JwtFilter jwtFilter;

    @Bean
    @ConditionalOnProperty(
            value = "default-idp",
            havingValue = "KEYCLOAK"
    )
    public SecurityFilterChain configSecurityWithExternalIdp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated();
        }).oauth2Login(oauth2Login -> oauth2Login.successHandler(oauth2SuccessHandler));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    @ConditionalOnProperty(
            value = "default-idp",
            havingValue = "default"
    )
    public SecurityFilterChain configSecurity(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/api/v1/auth/**").permitAll()
                    .anyRequest().authenticated();
        }).oauth2Login(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

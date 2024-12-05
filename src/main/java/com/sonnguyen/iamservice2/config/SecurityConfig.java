package com.sonnguyen.iamservice2.config;

import com.sonnguyen.iamservice2.security.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    JwtFilterImpl jwtFilterImpl;
    KeycloakJwtFilterImpl keycloakJwtFilter;
    LockAccountFilter lockAccountFilter;
    LoggingFilter loggingFilter;
    Oauth2SuccessHandler oauth2SuccessHandler;
    Oauth2Service oauth2Service;
    @Bean
    public SecurityFilterChain configSecurityWithExternalIdp(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> {
            request
                    .requestMatchers("/api/v1/auth/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**","/oauth2/**","/login/**").permitAll()
                    .anyRequest().authenticated();
        })
                .oauth2Login(customize->{
                    customize
                            .authorizationEndpoint(authorizationEndpointConfig -> {
                                authorizationEndpointConfig
                                        .baseUri("/oauth2/authorize");
                            })
                            .userInfoEndpoint(oauthUser->{
                                oauthUser.userService(oauth2Service);
                            })
                            .successHandler(oauth2SuccessHandler);
                });
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(jwtFilterImpl, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(keycloakJwtFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(lockAccountFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

package com.sonnguyen.iamservice2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@Configuration
public class JwtDecoderConfig {
    @Value("${keycloak.jwks_uri}")
    String jwksUri;

    @Bean
    @Order
    public JwtDecoder getJwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
    }
}

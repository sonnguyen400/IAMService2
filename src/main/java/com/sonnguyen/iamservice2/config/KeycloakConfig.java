package com.sonnguyen.iamservice2.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "default-idp",havingValue = "KEYCLOAK")
public class KeycloakConfig {
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;
    @Value("${keycloak.user_management.registration.username}")
    private String username;
    @Value("${keycloak.user_management.registration.password}")
    private String password;
    @Value("${keycloak.user_management.registration.realm}")
    private String realm;
    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(username)
                .password(password)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}

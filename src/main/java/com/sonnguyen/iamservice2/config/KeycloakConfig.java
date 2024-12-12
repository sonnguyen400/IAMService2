package com.sonnguyen.iamservice2.config;

import com.sonnguyen.iamservice2.viewmodel.KeycloakProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        value = "default-idp",
        havingValue = "KEYCLOAK"
)
@EnableConfigurationProperties(KeycloakProperties.class)
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class KeycloakConfig {
    KeycloakProperties keycloakProperties;

    @Bean
    Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.server_url())
                .clientId(keycloakProperties.client_id())
                .clientSecret(keycloakProperties.client_secret())
                .username(keycloakProperties.username())
                .password(keycloakProperties.password())
                .realm(keycloakProperties.realm())
                .grantType(OAuth2Constants.PASSWORD)
                .build();
    }
}

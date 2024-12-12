package com.sonnguyen.iamservice2.viewmodel;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
        String client_id,
        String client_secret,
        String server_url,
        String realm,
        String username,
        String password
) {
}

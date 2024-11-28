package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.*;
import jakarta.annotation.Nullable;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Primary
@ConditionalOnProperty(name = "default-idp",havingValue = "KEYCLOAK")
public class KeycloakAuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    private OAuth2AuthorizedClientService oauth2AuthorizedClientService;
    @Autowired
    KeycloakClientService keycloakClientService;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret}")
    private String clientSecret;

    @Override
    public ResponseEntity<?> login(@Nullable LoginPostVm loginPostVm) {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oauth2AuthorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        OAuth2RefreshToken oAuth2RefreshToken=oAuth2AuthorizedClient.getRefreshToken();
        String access_token=oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        String refresh_token=oAuth2RefreshToken!=null?oAuth2RefreshToken.getTokenValue():"";
        ResponseTokenVm responseBody = new ResponseTokenVm(access_token,refresh_token);
        return ResponseEntity.ok(responseBody);
    }

    @Override
    public ResponseTokenVm refreshToken(String refreshToken) {
        return keycloakClientService.refreshToken(Map.of(
                "refresh_token",refreshToken,
                "client_id",clientId,
                "client_secret",clientSecret,
                "grant_type","refresh_token"
        ));
    }
    public void logout(RequestTokenVm requestTokenVm) {
        keycloakClientService.logout(Map.of(
                "client_id",clientId,
                "client_secret",clientSecret,
                "refresh_token",requestTokenVm.refresh_token()
        ));
    }
}

package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.LoginResponseViewModel;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.admin.client.Keycloak;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Primary
@ConditionalOnBean(Keycloak.class)
public class KeycloakAuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    OAuth2AuthorizedClientService oauth2AuthorizedClientService;

    @Override
    public ResponseEntity<?> login(@Nullable LoginPostVm loginPostVm) {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient oAuth2AuthorizedClient = oauth2AuthorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        LoginResponseViewModel responseBody = new LoginResponseViewModel(oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        return ResponseEntity.ok(responseBody);
    }
}

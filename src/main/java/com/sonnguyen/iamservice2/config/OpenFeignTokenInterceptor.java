package com.sonnguyen.iamservice2.config;

import com.sonnguyen.iamservice2.service.keycloakClientCredentialTokenService;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class OpenFeignTokenInterceptor implements RequestInterceptor {
    keycloakClientCredentialTokenService keycloakClientCredentialTokenService;
    @Override
    public void apply(RequestTemplate requestTemplate) {
        String accessToken=keycloakClientCredentialTokenService.getClientCredentialAccessToken();
        requestTemplate.header("Authorization",String.format("Bearer %s",accessToken));
    }
}

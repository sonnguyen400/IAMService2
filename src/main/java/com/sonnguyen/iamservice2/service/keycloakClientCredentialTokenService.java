package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.KeycloakCredentialGetVm;
import com.sonnguyen.iamservice2.viewmodel.KeycloakProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class keycloakClientCredentialTokenService {
    RedisTemplate<String,Object> redisTemplate;
    KeycloakClientService keycloakClientService;
    KeycloakProperties keycloakProperties;
    public KeycloakCredentialGetVm getNewKeycloakCredentialGetVm() {
        return keycloakClientService.getClientCredentialsToken(Map.of("client_id",keycloakProperties.client_id(),
                "client_secret",keycloakProperties.client_secret(),
                "grant_type","client_credentials"));
    }
    public void save(KeycloakCredentialGetVm keycloakCredentialGetVm) {
        redisTemplate.opsForValue().set("client_credentials_access_token",keycloakCredentialGetVm.access_token(), Duration.ofSeconds(keycloakCredentialGetVm.expires_in()));
    }
    public String getClientCredentialAccessToken() {
        return (String) Optional.ofNullable(redisTemplate.opsForValue().get("client_credentials_access_token"))
                .orElseGet(()->{
                    KeycloakCredentialGetVm keycloakCredentialGetVm=getNewKeycloakCredentialGetVm();
                    save(keycloakCredentialGetVm);
                    return keycloakCredentialGetVm.access_token();
                });
    }
}

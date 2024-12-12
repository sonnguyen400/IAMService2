package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.constant.ActivityType;
import com.sonnguyen.iamservice2.model.UserActivityLog;
import com.sonnguyen.iamservice2.viewmodel.*;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Primary
@ConditionalOnProperty(
        value = "default-idp",
        havingValue = "KEYCLOAK"
)
public class KeycloakAuthenticationServiceImpl implements AuthenticationService {
    KeycloakClientService keycloakClientService;
    AccountService keycloakAccountService;
    UserActivityLogService logService;
    KeycloakProperties keycloakProperties;

    @Override
    public ResponseEntity<?> login(@Nullable LoginPostVm loginPostVm) {
        logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).build());
        return ResponseEntity.ok("Redirect to Keycloak to get token");
    }

    @Override
    public ResponseTokenVm refreshToken(String refreshToken) {
        return keycloakClientService.refreshToken(Map.of(
                "refresh_token", refreshToken,
                "client_id", keycloakProperties.client_id(),
                "client_secret", keycloakProperties.client_secret(),
                "grant_type", "refresh_token"
        ));
    }

    public void logout(RequestTokenVm requestTokenVm) {
        logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).build());
        keycloakClientService.logout(Map.of(
                "client_id", keycloakProperties.client_id(),
                "client_secret", keycloakProperties.client_secret(),
                "refresh_token", requestTokenVm.refresh_token()
        ));
    }

    public ResponseEntity<?> changePassword(ChangePasswordPostVm changePasswordPostVm) {
        logService.saveActivityLog(UserActivityLog.builder().activityType(ActivityType.LOGIN).build());
        keycloakAccountService.updatePasswordByEmail(changePasswordPostVm);
        return ResponseEntity.ok("Change password successfully");
    }
}

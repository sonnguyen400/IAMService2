package com.sonnguyen.iamservice2.viewmodel;

public record KeycloakCredentialGetVm(
        String access_token,
        Integer expires_in
) {
}

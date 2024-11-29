package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.ResponseTokenVm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "keycloakservice", url = "${keycloak.server-url}/realms/iam_service")
public interface KeycloakClientService {
    @PostMapping(value = "/protocol/openid-connect/token",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE )
    ResponseTokenVm refreshToken(Map<String,?> refreshToken);

    @PostMapping(value = "/protocol/openid-connect/logout",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void logout(@RequestBody Map<String,Object> logout);
}

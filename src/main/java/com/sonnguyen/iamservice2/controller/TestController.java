package com.sonnguyen.iamservice2.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.keycloak.admin.client.Keycloak;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    Keycloak keycloak;
    @GetMapping
    public ResponseEntity<?> keycloakKey(){
        return ResponseEntity.ok(keycloak.tokenManager().grantToken());
    }
}

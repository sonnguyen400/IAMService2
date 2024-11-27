package com.sonnguyen.iamservice2.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

@Component
public class KeycloakJwtUtilsImpl {
    @Autowired
    private JwtDecoder jwtDecoder;

    public Jwt validate(String token) {
        return jwtDecoder.decode(token);
    }

}

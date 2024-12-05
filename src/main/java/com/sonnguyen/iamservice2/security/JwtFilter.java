package com.sonnguyen.iamservice2.security;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;

public interface JwtFilter extends Filter {
    default String extractBearerTokenFromRequestHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) throw new RuntimeException("Authorization header is null");
        return authorizationHeader.replace("Bearer ", "");
    }
}

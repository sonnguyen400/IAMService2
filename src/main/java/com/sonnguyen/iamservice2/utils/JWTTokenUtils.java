package com.sonnguyen.iamservice2.utils;

import com.sonnguyen.iamservice2.viewmodel.ResponseTokenVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Component
public class JWTTokenUtils {
    @Autowired
    JWTUtilsImpl jwtUtils;
    public static final long ACCESS_TOKEN_EXPIRATION_SECOND = (long) (60 * 60 * 3);
    public static final long REFRESH_TOKEN_EXPIRATION_SECOND = (long) (60 * 60 * 12 * 7);

    public  ResponseTokenVm generateResponseToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        String access_token = generateAccessToken(subject, authorities);
        String refresh_token = generateRefreshToken(subject, authorities);
        return new ResponseTokenVm(access_token, refresh_token);
    }

    public  String generateRefreshToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authorities),
                "principal", subject
        );
        try {
            return jwtUtils.builder()
                    .subject(null)
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(REFRESH_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public  String generateAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authorities)
        );
        try {
            return jwtUtils.builder()
                    .subject(subject)
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static Collection<? extends GrantedAuthority> extractAuthoritiesFromString(String authorities) {
        if (authorities.isEmpty()) return List.of();
        List<String> scopes = Arrays.stream(authorities.split(" ")).toList();
        return scopes.stream().map(SimpleGrantedAuthority::new).toList();
    }

    public static String mapAuthoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }
}

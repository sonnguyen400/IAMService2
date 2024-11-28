package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.TokenException;
import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.LoginResponseViewModel;
import com.sonnguyen.iamservice2.viewmodel.RequestTokenVm;
import com.sonnguyen.iamservice2.viewmodel.ResponseTokenVm;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Order
public class AuthenticationServiceImpl implements AuthenticationService {
    public static final long ACCESS_TOKEN_EXPIRATION_SECOND = (long) (60 * 60 * 3);
    AuthenticationManager authenticationManager;
    JWTUtilsImpl jwtUtils;

    @Override
    public ResponseEntity<?> login(LoginPostVm loginPostVm) {
        String username = loginPostVm.username();
        String password = loginPostVm.password();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        ResponseTokenVm  responseBody = generateResponseToken((String) authentication.getPrincipal(),authentication.getAuthorities());
        return ResponseEntity.ok(responseBody);
    }

    public ResponseTokenVm refreshToken(String refreshToken){
        try {
            Claims claims=jwtUtils.validateToken(refreshToken);
            String scope=claims.get("scope").toString();
            String principal=claims.get("principal").toString();
            Collection<? extends GrantedAuthority> authorities=extractAuthoritiesFromString(scope);
            return generateResponseToken(principal,authorities);
        } catch (Exception e) {
            throw new TokenException(e.getMessage());
        }
    }


    public void logout(RequestTokenVm requestTokenVm){

    }
    public ResponseTokenVm generateResponseToken(String subject,Collection<? extends GrantedAuthority> authorities){
        String access_token=generateAccessToken(subject,authorities);
        String refresh_token=generateRefreshToken(subject,authorities);
        return new ResponseTokenVm(access_token,refresh_token);
    }
    public static String mapAuthoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }
    public String generateRefreshToken(String subject,Collection<? extends GrantedAuthority> authorities){
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authorities),
                "principal",subject
        );
        try {
            return jwtUtils.builder()
                    .subject(null)
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String generateAccessToken(String subject, Collection<? extends GrantedAuthority> authorities) {
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
}

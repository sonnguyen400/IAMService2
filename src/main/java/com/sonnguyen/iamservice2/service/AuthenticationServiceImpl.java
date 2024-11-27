package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.LoginResponseViewModel;
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
        String accesstoken = generateAccessToken(authentication);
        LoginResponseViewModel responseBody = new LoginResponseViewModel(accesstoken);
        return ResponseEntity.ok(responseBody);
    }

    public String generateAccessToken(Authentication authentication) {
        Map<String, Object> claims = Map.of(
                "scope", mapAuthoritiesToString(authentication.getAuthorities())
        );
        try {
            return jwtUtils.builder()
                    .subject(authentication.getName())
                    .claims(claims)
                    .expiration(Instant.now().plusSeconds(ACCESS_TOKEN_EXPIRATION_SECOND))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String mapAuthoritiesToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
    }

    public static Collection<? extends GrantedAuthority> extractAuthoritiesFromString(String authorities) {
        if (authorities.isEmpty()) return List.of();
        List<String> scopes = Arrays.stream(authorities.split(" ")).toList();
        return scopes.stream().map(SimpleGrantedAuthority::new).toList();
    }
}

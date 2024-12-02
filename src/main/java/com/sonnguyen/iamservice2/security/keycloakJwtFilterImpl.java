package com.sonnguyen.iamservice2.security;

import com.sonnguyen.iamservice2.model.UserDetails;
import com.sonnguyen.iamservice2.service.UserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "default-idp",
        havingValue = "KEYCLOAK"
)
@Primary
@Slf4j
public class keycloakJwtFilterImpl extends OncePerRequestFilter implements JwtFilter {
    JwtDecoder jwtDecoder;
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Jwt jwt = validateToken(request);
        if(jwt!=null){
            String username = jwt.getClaimAsString("preferred_username");
            try{
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                log.info("authorities {}", userDetails.getAuthorities());
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                log.info("username not found");
            }
        }
        filterChain.doFilter(request, response);
    }

    public Jwt validateToken(HttpServletRequest request) {
        try{
            String token = extractBearerTokenFromRequestHeader(request);
            return jwtDecoder.decode(token);
        }catch(Exception e){
            return null;
        }


    }
}

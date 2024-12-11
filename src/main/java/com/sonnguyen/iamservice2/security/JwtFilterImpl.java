package com.sonnguyen.iamservice2.security;

import com.sonnguyen.iamservice2.exception.TokenException;
import com.sonnguyen.iamservice2.service.ForbiddenTokenService;
import com.sonnguyen.iamservice2.utils.JWTTokenUtils;
import com.sonnguyen.iamservice2.utils.JWTUtilsImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@Order
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class JwtFilterImpl extends OncePerRequestFilter implements JwtFilter {
    JWTUtilsImpl jwtUtils;
    ForbiddenTokenService forbiddenTokenService;
    JWTTokenUtils jWTTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Claims claims = validateToken(request);
        if (claims != null && !claims.getSubject().isEmpty()) {
            Collection<? extends GrantedAuthority> authorities = JWTTokenUtils.extractAuthoritiesFromString(claims.get("scope", String.class));
            logger.info("User authorities: " + authorities);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
    private Claims validateToken(HttpServletRequest request) {
        try {
            String token = extractBearerTokenFromRequestHeader(request);
            if (forbiddenTokenService.findToken(token) != null) throw new TokenException("Invalid token");
            return jwtUtils.validateToken(token);
        } catch (Exception e) {
            return null;
        }
    }

}

package com.sonnguyen.iamservice2.security;

import com.sonnguyen.iamservice2.model.UserDetails;
import com.sonnguyen.iamservice2.service.UserDetailsService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationManager implements AuthenticationManager {
    UserDetailsService userDetailsService;
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());
        if (!userDetails.isVerified()) throw new BadCredentialsException("User is not verified");
        if (!userDetails.isNonLocked()) throw new LockedException("User is locked");
        if (passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
            return auth;
        }
        throw new BadCredentialsException("Bad credentials");
    }
    public Authentication oauth2Authenticate(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (!userDetails.isVerified()) throw new BadCredentialsException("User is not verified");
        if (!userDetails.isNonLocked()) throw new LockedException("User is locked");
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }
}

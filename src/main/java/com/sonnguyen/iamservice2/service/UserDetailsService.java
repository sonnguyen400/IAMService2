package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.model.UserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserDetailsService {
    AccountServiceImpl userService;

    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        return userService
                .findByEmail(username)
                .map(UserDetailsService::fromUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public static UserDetails fromUser(Account account) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(account.getEmail());
        userDetails.setPassword(account.getPassword());
        userDetails.setVerified(account.getVerified());
        userDetails.setNonLocked(!account.getLocked());
        userDetails.setAuthorities(List.of());
        return userDetails;
    }
}

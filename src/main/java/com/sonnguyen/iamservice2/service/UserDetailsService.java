package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.User;
import com.sonnguyen.iamservice2.model.UserDetails;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserDetailsService {
    UserServiceImpl userService;

    public UserDetails loadUserByUsername(@NotNull String username) throws UsernameNotFoundException {
        return userService
                .findByUsername(username)
                .map(UserDetailsService::fromUser)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public static UserDetails fromUser(User user) {
        UserDetails userDetails = new UserDetails();
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        return userDetails;
    }
}

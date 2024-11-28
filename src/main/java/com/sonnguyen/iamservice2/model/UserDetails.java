package com.sonnguyen.iamservice2.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
public class UserDetails {
    private String username;
    private String password;
    private boolean enabled;
    private boolean nonLocked;
    private Collection<? extends GrantedAuthority> authorities;
}

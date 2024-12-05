package com.sonnguyen.iamservice2.security;

import com.sonnguyen.iamservice2.exception.ResourceNotFoundException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.service.AccountServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Oauth2Service extends DefaultOAuth2UserService {
    AccountServiceImpl accountService;
    UsernamePasswordAuthenticationManager authenticationManager;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User=super.loadUser(userRequest);
        String email=oAuth2User.getAttribute("email");
        if(email!=null){
            try{
                authenticationManager.oauth2Authenticate(email);
            } catch (ResourceNotFoundException e) {
                Account account=Account
                        .builder()
                        .email(email)
                        .password(UUID.randomUUID().toString())
                        .verified(true)
                        .picture((String) oAuth2User.getAttributes().get("picture"))
                        .build();
                accountService.saveAccount(account);
            }
        }
        return oAuth2User;
    }


}

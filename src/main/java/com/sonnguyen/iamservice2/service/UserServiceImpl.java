package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.User;
import com.sonnguyen.iamservice2.repository.UserRepository;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public void createUser(UserRegistrationPostVm userRegistrationPostVm) {
        User user = userRegistrationPostVm.toEntity();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}

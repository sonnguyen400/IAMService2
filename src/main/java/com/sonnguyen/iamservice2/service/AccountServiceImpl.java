package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.ResourceNotFoundException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.repository.AccountRepository;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    public Optional<Account> findByEmail(String username) {
        return accountRepository.findByEmail(username);
    }
    @Override
    public void createUser(UserRegistrationPostVm userRegistrationPostVm) {
        Account account = userRegistrationPostVm.toEntity();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }
    @Override
    @Transactional
    public void enableAccountByEmail(String email) {
        System.out.println("Enable "+email);
        accountRepository.enableAccountByEmail(email);
    }
}

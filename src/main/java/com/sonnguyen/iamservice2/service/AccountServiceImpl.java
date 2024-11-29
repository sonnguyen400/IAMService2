package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.DuplicatedException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.repository.AccountRepository;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public void register(UserRegistrationPostVm userRegistrationPostVm){
        if(accountRepository.existsAccountByEmail(userRegistrationPostVm.email())){
            throw new DuplicatedException("Email was registered");
        }
        Account account = userRegistrationPostVm.toEntity();
        account.setVerified(false);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }
    @Override
    @Transactional
    public void updateLockedStatusByEmail(Boolean isLocked, String email) {
        accountRepository.updateAccountLockStatusByEmail(isLocked,email);
    }
    @Override
    public void create(UserCreationPostVm userCreationPostVm) {
        if(accountRepository.existsAccountByEmail(userCreationPostVm.email())){
            throw new DuplicatedException("Email was registered");
        }
        Account account = userCreationPostVm.toEntity();
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);
    }

    @Transactional
    public void verifyAccountByEmail(String email) {
        accountRepository.verifiedAccountByEmail(email);
    }

    public Page<UserDetailGetVm> findAll(Pageable pageable){
        return accountRepository.findAll(pageable).map(UserDetailGetVm::fromEntity);
    }
}

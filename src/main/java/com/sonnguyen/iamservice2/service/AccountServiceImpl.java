package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.DuplicatedException;
import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.repository.AccountRepository;
import com.sonnguyen.iamservice2.specification.AccountSpecification;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import com.sonnguyen.iamservice2.viewmodel.UserProfilePostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;
    PasswordEncoder passwordEncoder;
    public Account findById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(()-> new NotFoundException("Account not found"));
    }
    public Optional<Account> findByEmail(String username) {
        return accountRepository.findByEmail(username);
    }
    @Override
    public void register(UserRegistrationPostVm userRegistrationPostVm){
        Account account = userRegistrationPostVm.toEntity();
        account.setVerified(false);
        saveAccount(account);
    }

    @Override
    @Transactional
    public void updateLockedStatusByEmail(Boolean isLocked, String email) {
        accountRepository.updateAccountLockStatusByEmail(isLocked,email);
    }
    @Override
    public void create(UserCreationPostVm userCreationPostVm) {
        Account account = userCreationPostVm.toEntity();
        saveAccount(account);
    }
    @Transactional
    public void verifyAccountByEmail(String email) {
        accountRepository.verifiedAccountByEmail(email);
    }
    public Account saveAccount(Account account){
        if(accountRepository.existsAccountByEmail(account.getEmail())){
            throw new DuplicatedException("Email was registered");
        }
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }
    @Override
    @Transactional
    public ResponseEntity<?> deleteByEmail(String email){
        if(existedByEmail(email)){
            accountRepository.softDeleteByEmail(email);
        }
        return ResponseEntity.status(Response.Status.NOT_FOUND.getStatusCode())
                .body("User registered with email"+email+"not found");
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteById(Object id) {
        accountRepository.softDeleteById((Long) id);
        return ResponseEntity.ok().build();
    }

    public boolean existedByEmail(String email){
        return accountRepository.existsAccountByEmail(email);
    }
    public Page<UserDetailGetVm> findAll(Pageable pageable){
        return accountRepository.findAll(pageable).map(UserDetailGetVm::fromEntity);
    }
    public Page<UserDetailGetVm> findAll(List<AccountSpecification> specifications, Pageable pageable){
        if(!specifications.isEmpty()){
            Specification<Account> predicates= specifications.getFirst();
            for(int i=1;i<specifications.size();i++){
                predicates=predicates.and(specifications.get(i));
            }
            return accountRepository.findAll(predicates,pageable).map(UserDetailGetVm::fromEntity);
        }
        return accountRepository.findAll(pageable).map(UserDetailGetVm::fromEntity);
    }
    @Override
    @Transactional
    public void resetPasswordByAccountId(Long accountId, String rawPassword){
        String password=passwordEncoder.encode(rawPassword);
        accountRepository.resetPasswordByAccountId(accountId,password);
    }

    public void updateAccountProfileById(Long accountId, UserProfilePostVm userProfilePostVm){
        Account oldAccount=findById(accountId);
        Account newAccount=mapNewAccountProfile(oldAccount,userProfilePostVm);
        accountRepository.save(newAccount);
    }
    private Account mapNewAccountProfile(Account oldAccount,UserProfilePostVm userProfilePostVm){
        oldAccount.setFirstName(userProfilePostVm.firstname());
        oldAccount.setLastName(userProfilePostVm.lastname());
        oldAccount.setAddress(userProfilePostVm.address());
        oldAccount.setPhone(userProfilePostVm.phone());
        oldAccount.setDateOfBirth(userProfilePostVm.dateOfBirth());
        return oldAccount;
    }


}

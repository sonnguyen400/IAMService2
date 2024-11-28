package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsAccountByEmail(String email);
    Optional<Account> findByEmail(String email);
    @Modifying
    @Query("update Account a set a.verified=true where a.email=?1")
    void verifiedAccountByEmail(String email);
}

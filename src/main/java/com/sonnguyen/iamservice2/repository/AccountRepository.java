package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Account;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select case when exists (select a from Account a where a.email=?1 and (a.deleted=false or a.deleted is null )) then true else false end as account_exists")
    boolean existsAccountByEmail(String email);

    @Query("select a from Account a where a.email=?1 and (a.deleted=false or a.deleted is null)")
    Optional<Account> findByEmail(String email);

    @Modifying
    @Query("update Account a set a.verified=true where a.email=?1")
    void verifiedAccountByEmail(String email);

    @Modifying
    @Query("update Account a set a.locked=?1 where a.email=?2")
    void updateAccountLockStatusByEmail(Boolean lockedStatus, String email);

    @NonNull
    @Query(value = "select a from Account a where a.deleted is null or a.deleted=false")
    Page<Account> findAll(@NonNull Pageable pageable);

    @NonNull
    Page<Account> findAll(@NonNull Specification<Account> specification, @NonNull Pageable pageable);

    @Query(value = "update Account a set a.deleted=true where a.email=?1")
    @Modifying
    void softDeleteByEmail(String email);

    @Query(value = "update Account a set a.deleted=true where a.id=?1")
    @Modifying
    void softDeleteById(Long id);

    @Modifying
    @Query(value = "update Account a set a.password=?2 where a.id=?1")
    void resetPasswordByAccountId(Long accountId, String encodedPassword);

    @Modifying
    @Query(value = "update Account a set a.picture=?2 where a.id=?1")
    void updateAccountPictureByAccountID(Long accountId, String pictureUrl);

    @Modifying
    @Query(value = "update Account a set a.password=?2 where a.email=?1")
    void updatePasswordByEmail(String email, String encodedPassword);
    @NonNull
    List<Account> findAll(Specification<Account> specification);
}

package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.AccountRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    @Query(value = "Select ar from AccountRole ar where ar.account_id=?1 and ar.deleted=false")
    List<AccountRole> findAllByAccountId(Long id);

    @Modifying
    @Query(value = "update AccountRole ar set ar.deleted=true where ar.account_id= :account_id and ar.role_id not in :role_ids")
    void deleteByAccountIdAndUpdateRole(Long account_id, List<Long> role_ids);
}

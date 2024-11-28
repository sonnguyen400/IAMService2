package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Role;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByIdIn(Collection<Long> ids);
    boolean existsByName(String name);
    @Modifying
    @Query("update Role r set r.deleted=false  where r.id=?1")
    void softDeleteById(Long id);
    @Modifying
    @Query("update Role r set r.deleted=false  where r.name=?1")
    void softDeleteByName(String name);
    @Modifying
    @Query("update Role r set r.name=?1 where r.id=?2")
    void updateNameById(String name,Long id);
    @Query("select r from Role r where r.deleted=false")
    @NonNull
    Page<Role> findAll(@NonNull Pageable pageable);
}

package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Permission;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("select p from Permission p where p.id in :ids and p.deleted=false")
    List<Permission> findAllByIdIn(@Param("ids") List<Long> id);

    @Nonnull
    @Query("select p from Permission p where p.deleted=false")
    Page<Permission> findAll(@Nonnull Pageable pageable);

    @Modifying
    @Query("update Permission p set p.deleted=true where p.id=?1")
    void softDeleteById(Long id);
}

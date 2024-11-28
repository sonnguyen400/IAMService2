package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Permission;
import com.sonnguyen.iamservice2.model.Role;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {
    @Query("select r from Permission r where r.deleted=false")
    @NonNull
    Page<Permission> findAll(@NonNull Pageable pageable);
}

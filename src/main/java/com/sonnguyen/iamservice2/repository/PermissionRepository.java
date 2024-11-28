package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,Long> {

}

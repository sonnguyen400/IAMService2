package com.sonnguyen.iamservice2.repository;

import com.sonnguyen.iamservice2.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    @Query("select p from RolePermission p where p.role_id=?1 and p.deleted=false")
    List<RolePermission> findAllByRoleId(Long roleId);

    @Modifying
    @Query("update RolePermission p set p.deleted=true where p.id in :ids")
    void softDeleteAllWhereIdIn(@Param("ids") List<Long> id);

    @Modifying
    @Query("update RolePermission p set p.deleted=true where p.role_id= :roleId and p.permission_id not in :permissionIds")
    void deleteByRoleIdAndUpdatedPermission(@Param("roleId") Long role_id, @Param("permissionIds") List<Long> permissionId);

    @Modifying
    @Query("update RolePermission set deleted=true where role_id=?1")
    void softDeleteByRoleId(Long roleId);

    @Modifying
    @Query("select r from RolePermission r where r.role_id in :roleIds and r.deleted=false")
    List<RolePermission> findAllByRoleIdIn(@Param("roleIds") List<Long> roleIds);
}

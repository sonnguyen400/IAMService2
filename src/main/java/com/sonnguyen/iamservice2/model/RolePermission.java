package com.sonnguyen.iamservice2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;

@Entity
@Data
public class RolePermission extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long permission_id;
    private Long role_id;
    private String resource_code;
    private String scope;
    @ColumnDefault(value = "false")
    private boolean deleted;
}

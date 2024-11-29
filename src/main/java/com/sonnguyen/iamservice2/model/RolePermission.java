package com.sonnguyen.iamservice2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DialectOverride;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

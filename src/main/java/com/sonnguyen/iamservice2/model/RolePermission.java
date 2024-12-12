package com.sonnguyen.iamservice2.model;

import com.sonnguyen.iamservice2.constant.Scope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePermission extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long permission_id;
    private Long role_id;
    private String resource_code;
    @Enumerated(EnumType.STRING)
    private Scope scope;
    @ColumnDefault(value = "false")
    private boolean deleted;
}

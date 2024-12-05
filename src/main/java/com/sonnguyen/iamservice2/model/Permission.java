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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String resource_name;
    private String resource_code;
    @Enumerated(EnumType.STRING)
    private Scope scope;
    @ColumnDefault(value = "false")
    private boolean deleted;
}

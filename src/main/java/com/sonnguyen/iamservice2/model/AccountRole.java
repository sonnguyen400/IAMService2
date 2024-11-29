package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
public class AccountRole extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long role_id;
    private Long account_id;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
}

package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRole extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long role_id;
    private Long account_id;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    public AccountRole(Long role_id, Long account_id) {
        this.role_id = role_id;
        this.account_id = account_id;
    }
}

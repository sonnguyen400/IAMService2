package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Data
public class Role extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private String resource_name;
    private String resource_code;
    private String scope;
    @ColumnDefault(value = "false")
    private boolean deleted;
}

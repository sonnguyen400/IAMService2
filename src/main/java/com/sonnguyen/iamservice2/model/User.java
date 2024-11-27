package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "user_data")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String email;
}

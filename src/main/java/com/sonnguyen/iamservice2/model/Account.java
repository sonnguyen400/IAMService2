package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class Account extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String picture;
    private Date dateOfBirth;
    @ColumnDefault(value = "false")
    private boolean locked;
    @ColumnDefault(value = "true")
    private boolean verified = true;
    @ColumnDefault(value = "false")
    private boolean deleted;
}

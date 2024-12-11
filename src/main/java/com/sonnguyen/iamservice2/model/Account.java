package com.sonnguyen.iamservice2.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@ToString
public class Account extends AbstractAuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    @Email
    private String email;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String phone;
    private String address;
    private String picture;
    @NotNull
    private Date dateOfBirth;
    private Integer yearOfExperience;
    @NotBlank
    private String street;
    @NotBlank
    private String commune;
    @NotBlank
    private String province;
    @NotBlank
    private String district;
    @ColumnDefault(value = "false")
    private boolean locked;
    @ColumnDefault(value = "true")
    private boolean verified = true;
    @ColumnDefault(value = "false")
    private boolean deleted;

    public Integer getYearOfExperience() {
        return yearOfExperience==null?0:yearOfExperience;
    }


}

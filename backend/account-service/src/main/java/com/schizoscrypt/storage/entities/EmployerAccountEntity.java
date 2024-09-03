package com.schizoscrypt.storage.entities;

import com.schizoscrypt.storage.abstr.AccountEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employer_account")
public class EmployerAccountEntity extends AccountEntity {

    @Column(name = "company")
    private String companyName;

    private String firstname;

    private String lastname;

    private String address;

    private String industry;

    @Column(name = "website")
    private String companyWebsite;

    @Column(name = "description")
    private String companyDescription;
}

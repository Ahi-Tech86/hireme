package com.schizoscrypt.storage.entities;

import com.schizoscrypt.storage.abstr.AccountEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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

    public static class EmployerAccountEntityBuilder {
        private String email;
        private String phoneNumber;
        private LocalDate createAt;

        public EmployerAccountEntityBuilder email(String email) {
            this.email = email;
            return this;
        }

        public EmployerAccountEntityBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public EmployerAccountEntityBuilder createAt(LocalDate createAt) {
            this.createAt = createAt;
            return this;
        }

        public EmployerAccountEntity build() {
            EmployerAccountEntity entity = new EmployerAccountEntity();
            entity.setEmail(email);
            entity.setCreateAt(createAt);
            entity.setPhoneNumber(phoneNumber);
            entity.address = this.address;
            entity.lastname = this.lastname;
            entity.industry = this.industry;
            entity.firstname = this.firstname;
            entity.companyName = this.companyName;
            entity.companyWebsite = this.companyWebsite;
            entity.companyDescription = this.companyDescription;

            return entity;
        }
    }
}

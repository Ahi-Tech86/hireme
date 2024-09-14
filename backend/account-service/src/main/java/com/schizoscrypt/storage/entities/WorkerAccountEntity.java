package com.schizoscrypt.storage.entities;

import com.schizoscrypt.storage.abstr.AccountEntity;
import com.schizoscrypt.storage.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "worker_account")
public class WorkerAccountEntity extends AccountEntity {

    private String firstname;

    private String lastname;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public static class WorkerAccountEntityBuilder {
        private String email;
        private String phoneNumber;
        private LocalDate createAt;

        public WorkerAccountEntityBuilder email(String email) {
            this.email = email;
            return this;
        }

        public WorkerAccountEntityBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public WorkerAccountEntityBuilder createAt(LocalDate createAt) {
            this.createAt = createAt;
            return this;
        }

        public WorkerAccountEntity build() {
            WorkerAccountEntity entity = new WorkerAccountEntity();
            entity.setEmail(email);
            entity.setPhoneNumber(phoneNumber);
            entity.setCreateAt(createAt);
            entity.firstname = this.firstname;
            entity.lastname = this.lastname;
            entity.birthdate = this.birthdate;
            entity.gender = this.gender;
            return entity;
        }
    }
}

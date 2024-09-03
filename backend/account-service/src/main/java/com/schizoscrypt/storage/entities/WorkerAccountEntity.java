package com.schizoscrypt.storage.entities;

import com.schizoscrypt.storage.abstr.AccountEntity;
import com.schizoscrypt.storage.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

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

    private Date birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;
}

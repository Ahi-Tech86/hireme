package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.storage.entities.WorkerAccountEntity;
import com.schizoscrypt.storage.enums.Gender;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class WorkerAccountEntityFactory {

    public WorkerAccountEntity makeWorkerAccountEntity(
            String email, String firstname, String lastname, String phoneNumber, LocalDate birthdate, Gender gender
    ) {
        return WorkerAccountEntity.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .birthdate(birthdate)
                .gender(gender)
                .phoneNumber(phoneNumber)
                .createAt(LocalDate.now())
                .build();
    }
}

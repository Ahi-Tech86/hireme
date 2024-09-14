package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.WorkerAccountDto;
import com.schizoscrypt.storage.entities.WorkerAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class WorkerAccountDtoFactory {

    public WorkerAccountDto makeWorkerAccountDto(WorkerAccountEntity entity) {

        return WorkerAccountDto.builder()
                .email(entity.getEmail())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .phoneNumber(entity.getPhoneNumber())
                .birthdate(entity.getBirthdate())
                .createAt(entity.getCreateAt())
                .gender(entity.getGender())
                .build();
    }
}

package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.EmployerAccountDto;
import com.schizoscrypt.storage.entities.EmployerAccountEntity;
import org.springframework.stereotype.Component;

@Component
public class EmployerAccountDtoFactory {

    public EmployerAccountDto makeEmployerAccountDto(EmployerAccountEntity entity) {
        return EmployerAccountDto.builder()
                .email(entity.getEmail())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .phoneNumber(entity.getPhoneNumber())
                .createAt(entity.getCreateAt())
                .industry(entity.getIndustry())
                .address(entity.getAddress())
                .companyDescription(entity.getCompanyDescription())
                .companyName(entity.getCompanyName())
                .companyWebsite(entity.getCompanyWebsite())
                .build();
    }
}

package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.CreateEmployerAccountRequestDto;
import com.schizoscrypt.storage.entities.EmployerAccountEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmployerAccountEntityFactory {

    public EmployerAccountEntity makeEmployerAccountEntity(
            String email, String firstname, String lastname, String phoneNumber, CreateEmployerAccountRequestDto request
    ) {
        return EmployerAccountEntity.builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .phoneNumber(phoneNumber)
                .industry(request.getIndustryName())
                .address(request.getAddress())
                .companyWebsite(request.getCompanyWebsite())
                .companyDescription(request.getCompanyDescription())
                .createAt(LocalDate.now())
                .companyName(request.getCompanyName())
                .build();
    }
}

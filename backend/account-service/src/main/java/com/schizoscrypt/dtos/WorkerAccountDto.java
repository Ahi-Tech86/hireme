package com.schizoscrypt.dtos;

import com.schizoscrypt.storage.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerAccountDto {
    private String email;
    private String firstname;
    private String lastname;
    private Date birthdate;
    private String phoneNumber;
    private Date createAt;
    private Gender gender;
}

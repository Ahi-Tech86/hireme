package com.schizoscrypt.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.schizoscrypt.storage.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkerAccountDto {
    private String email;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("create_at")
    private LocalDate createAt;
    private Gender gender;
}

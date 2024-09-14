package com.schizoscrypt.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerAccountDto {
    private String email;
    private String firstname;
    private String lastname;
    @JsonProperty("phone_number")
    private String phoneNumber;
    @JsonProperty("create_at")
    private LocalDate createAt;
    @JsonProperty("name")
    private String companyName;
    private String address;
    private String industry;
    @JsonProperty("website")
    private String companyWebsite;
    @JsonProperty("description")
    private String companyDescription;
}

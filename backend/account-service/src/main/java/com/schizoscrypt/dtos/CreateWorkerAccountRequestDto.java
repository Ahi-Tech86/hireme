package com.schizoscrypt.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkerAccountRequestDto {

    @JsonProperty("phone_number")
    private String phoneNumber;

    private String birthdate;

    private String gender;
}

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
public class CreateEmployerAccountRequestDto {

    @JsonProperty("company_name")
    private String companyName;

    private String address;

    @JsonProperty("industry_name")
    private String industryName;

    private String companyWebsite;

    @JsonProperty("company_description")
    private String companyDescription;
}

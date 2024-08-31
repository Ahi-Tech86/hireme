package com.schizoscrypt.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenDto {
    private String firstname;
    private String lastname;
    private String email;
    private String accessToken;
    private String refreshToken;
}

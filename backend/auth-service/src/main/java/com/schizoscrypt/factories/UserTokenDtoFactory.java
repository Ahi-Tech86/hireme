package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.UserTokenDto;
import org.springframework.stereotype.Component;

@Component
public class UserTokenDtoFactory {

    public UserTokenDto makeUserTokenDto(UserDto userDto, String accessToken, String refreshToken) {

        return UserTokenDto.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.AuthenticatedUserDto;
import com.schizoscrypt.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedUserDtoFactory {

    public AuthenticatedUserDto makeUserTokenDto(UserDto userDto, String accessToken, String refreshToken) {

        return AuthenticatedUserDto.builder()
                .firstname(userDto.getFirstname())
                .lastname(userDto.getLastname())
                .email(userDto.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}

package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.UserDto;
import org.springframework.stereotype.Component;
import com.schizoscrypt.storage.entities.UserEntity;

@Component
public class UserDtoFactory {

    public UserDto makeUserDto(UserEntity user) {

        return UserDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}

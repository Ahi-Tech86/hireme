package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.UserAccountDto;
import com.schizoscrypt.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserAccountDtoFactory {

    public UserAccountDto makeUserAccountDto(UserEntity user) {

        return UserAccountDto.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .build();
    }
}

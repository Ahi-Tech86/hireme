package com.schizoscrypt.factories;

import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.storage.enums.AppRole;
import org.springframework.stereotype.Component;
import com.schizoscrypt.storage.entities.UserEntity;

@Component
public class UserEntityFactory {

    public UserEntity makeUserEntity(RegisterRequest request, AppRole role) {

        return UserEntity.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(role)
                .build();
    }
}

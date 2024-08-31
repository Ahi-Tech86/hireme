package com.schizoscrypt.factories;

import com.schizoscrypt.storage.entities.RefreshTokenEntity;
import com.schizoscrypt.storage.entities.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RefreshTokenEntityFactory {

    public RefreshTokenEntity makeRefreshTokenEntity(UserEntity user, String token, Date expirationTime) {

        return RefreshTokenEntity.builder()
                .token(token)
                .user(user)
                .createAt(new Date(System.currentTimeMillis()))
                .expiresAt(expirationTime)
                .build();
    }
}

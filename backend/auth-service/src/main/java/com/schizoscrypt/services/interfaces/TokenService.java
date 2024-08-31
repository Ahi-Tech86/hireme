package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.storage.entities.UserEntity;

public interface TokenService {

    void createAndSaveToken(UserEntity user);
}

package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.storage.enums.AppRole;

public interface JwtService {

    String extractSubject(String token);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    String generateAccessToken(String email, AppRole role);
}

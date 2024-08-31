package com.schizoscrypt.services;

import com.schizoscrypt.storage.enums.AppRole;
import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(String email, AppRole role);

    String generateRefreshToken(String email, AppRole role);

    Authentication validateAccessToken(String token);

    Authentication validateRefreshToken(String token);
}

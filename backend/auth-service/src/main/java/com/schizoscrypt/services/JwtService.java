package com.schizoscrypt.services;

import org.springframework.security.core.Authentication;

public interface JwtService {
    String generateAccessToken(String email);

    String generateRefreshToken(String email);

    Authentication validateAccessToken(String token);

    Authentication validateRefreshToken(String token);
}

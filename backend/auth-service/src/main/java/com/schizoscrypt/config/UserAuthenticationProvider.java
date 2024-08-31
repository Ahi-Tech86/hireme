package com.schizoscrypt.config;

import lombok.RequiredArgsConstructor;
import com.schizoscrypt.services.JwtServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    private final JwtServiceImpl jwtService;

    public String createAccessToken(String email) {
        return jwtService.generateAccessToken(email);
    }

    public String createRefreshToken(String email) {
        return jwtService.generateRefreshToken(email);
    }

    public Authentication validateAccessToken(String token) {
        return jwtService.validateAccessToken(token);
    }

    public Authentication validateRefreshToken(String token) {
        return jwtService.validateRefreshToken(token);
    }
}

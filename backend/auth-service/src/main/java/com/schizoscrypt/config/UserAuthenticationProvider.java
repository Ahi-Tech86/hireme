package com.schizoscrypt.config;

import com.schizoscrypt.storage.enums.AppRole;
import lombok.RequiredArgsConstructor;
import com.schizoscrypt.services.JwtServiceImpl;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider {

    private final JwtServiceImpl jwtService;

    public String createAccessToken(String email, AppRole role) {
        return jwtService.generateAccessToken(email, role);
    }

    public String createRefreshToken(String email, AppRole role) {
        return jwtService.generateRefreshToken(email, role);
    }

    public Authentication validateAccessToken(String token) {
        return jwtService.validateAccessToken(token);
    }

    public Authentication validateRefreshToken(String token) {
        return jwtService.validateRefreshToken(token);
    }
}

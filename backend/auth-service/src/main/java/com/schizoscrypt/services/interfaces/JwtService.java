package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.storage.enums.AppRole;
import org.springframework.security.core.Authentication;

import java.util.Date;

public interface JwtService {

    boolean isAccessTokenExpired(String token);

    boolean isRefreshTokenExpired(String token);

    Authentication validateAccessToken(String token);

    Authentication validateRefreshToken(String token);

    Date extractAccessTokenExpirationTime(String token);

    Date extractRefreshTokenExpirationTime(String token);

    String generateAccessToken(String email, AppRole role);

    String generateRefreshToken(String email, AppRole role);
}

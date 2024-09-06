package com.schizoscrypt.services;

import com.schizoscrypt.services.interfaces.JwtService;
import com.schizoscrypt.storage.enums.AppRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.access-token.secret-key}")
    private String secretKeyToAccessToken;
    @Value("${application.security.jwt.access-token.expiration}")
    private Long accessTokenExpirationTime;
    @Value("${application.security.jwt.refresh-token.secret-key}")
    private String secretKeyToRefreshToken;

    private Key accessSignKey;
    private Key refreshSignKey;

    @PostConstruct
    private void init() {
        accessSignKey = getSignKeyForAccess();
        refreshSignKey = getSignKeyForRefresh();
    }

    @Override
    public String extractSubject(String token) {

        Claims claims = extractAllClaims(token, accessSignKey);

        return claims.getSubject();
    }

    @Override
    public String generateAccessToken(String email, AppRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(accessSignKey)
                .compact();
    }

    @Override
    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSignKey);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSignKey);
    }

    private Key getSignKeyForAccess() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyToAccessToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignKeyForRefresh() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyToRefreshToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private boolean validateToken(String token, Key key) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = extractAllClaims(token, key);
            Date expiration = claims.getExpiration();

            if (expiration.before(new Date())) {
                return true;
            }
        } catch (RuntimeException exception) {
            System.out.println("Invalid JWT signature");
        }

        return false;
    }

    private Claims extractAllClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

package com.schizoscrypt.filters.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {

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

    public boolean validateAccessToken(String token) {
        return validateToken(token, accessSignKey);
    }

    public boolean validateRefreshToken(String token) {
        return validateToken(token, refreshSignKey);
    }

    public boolean isAccessTokenExpired(String token) {
        return isTokenExpired(token, accessSignKey);
    }

    public boolean isRefreshTokenExpired(String token) {
        return isTokenExpired(token, refreshSignKey);
    }

    public String generateToken(String email, AppRole role) {
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

    public AppRole extractRoleFromRefreshToken(String token) {
        Claims claims = extractAllClaims(token, refreshSignKey);
        return claims.get("role", AppRole.class);
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

            return true;

        } catch (RuntimeException exception) {
            System.out.println("Invalid JWT signature");
        }

        return false;
    }

    private boolean isTokenExpired(String token, Key key) {
        Claims claims = extractAllClaims(token, key);

        Date expirationDate = claims.getExpiration();

        return expirationDate.before(new Date());
    }

    private Claims extractAllClaims(String token, Key key) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

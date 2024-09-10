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
    public static String secretKeyToAccessToken;
    @Value("${application.security.jwt.access-token.expiration}")
    private static Long accessTokenExpirationTime;
    @Value("${application.security.jwt.refresh-token.secret-key}")
    private static String secretKeyToRefreshToken;

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

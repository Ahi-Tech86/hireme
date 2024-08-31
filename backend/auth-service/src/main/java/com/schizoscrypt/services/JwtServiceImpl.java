package com.schizoscrypt.services;

import com.schizoscrypt.services.interfaces.JwtService;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.enums.AppRole;
import com.schizoscrypt.storage.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collections;
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
    @Value("${application.security.jwt.refresh-token.expiration}")
    private Long refreshTokenExpirationTime;

    private Key accessSignKey;
    private Key refreshSignKey;
    private final UserRepository repository;

    @PostConstruct
    public void init() {
        accessSignKey = getSignKeyForAccess();
        refreshSignKey = getSignKeyForRefresh();
    }

    @Override
    public Date extractAccessTokenExpirationTime(String token) {
        return extractExpiration(token, accessSignKey);
    }

    @Override
    public Date extractRefreshTokenExpirationTime(String token) {
        return extractExpiration(token, refreshSignKey);
    }

    @Override
    public String generateAccessToken(String email, AppRole role) {
        return createToken(email, accessTokenExpirationTime, accessSignKey, role);
    }

    @Override
    public String generateRefreshToken(String email, AppRole role) {
        return createToken(email, refreshTokenExpirationTime, refreshSignKey, role);
    }

    @Override
    public Authentication validateAccessToken(String token) {
        return validateToken(token, accessSignKey);
    }

    @Override
    public Authentication validateRefreshToken(String token) {
        return validateToken(token, refreshSignKey);
    }

    @Override
    public boolean isAccessTokenExpired(String token) {
        return expirationValidate(token, accessSignKey);
    }

    @Override
    public boolean isRefreshTokenExpired(String token) {
        return expirationValidate(token, refreshSignKey);
    }

    private Key getSignKeyForAccess() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyToAccessToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSignKeyForRefresh() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKeyToRefreshToken);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Date extractExpiration(String token, Key key) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    private boolean expirationValidate(String token, Key key) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiredDate = claims.getExpiration();

        if (expiredDate.before(new Date())) {
            return true;
        } else {
            return false;
        }
    }

    private Authentication validateToken(String token, Key key) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();

        String email = claims.getSubject();
        UserEntity user = repository.findByEmail(email).orElseThrow(

        );

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    private String createToken(String email, Long expiration, Key key, AppRole role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}

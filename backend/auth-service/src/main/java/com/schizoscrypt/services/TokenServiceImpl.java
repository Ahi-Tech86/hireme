package com.schizoscrypt.services;

import com.schizoscrypt.factories.RefreshTokenEntityFactory;
import com.schizoscrypt.services.interfaces.TokenService;
import com.schizoscrypt.storage.entities.RefreshTokenEntity;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final JwtServiceImpl jwtService;
    private final TokenRepository repository;
    private final RefreshTokenEntityFactory factory;
    private final EncryptionServiceImpl encryptionService;

    @Override
    @Transactional
    public void createAndSaveToken(UserEntity user) {

        // generating refresh token and extract expiration time
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getRole());
        Date expirationTime = jwtService.extractRefreshTokenExpirationTime(refreshToken);

        // encrypting token before save in db
        String encryptedToken = encryptionService.encrypt(refreshToken);

        RefreshTokenEntity token = factory.makeRefreshTokenEntity(user, encryptedToken, expirationTime);

        repository.saveAndFlush(token);
        log.info("Refresh token for user with {} email was successfully saved", user.getEmail());
    }
}

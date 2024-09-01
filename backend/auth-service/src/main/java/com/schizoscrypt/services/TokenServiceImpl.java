package com.schizoscrypt.services;

import com.schizoscrypt.exception.AppException;
import com.schizoscrypt.factories.RefreshTokenEntityFactory;
import com.schizoscrypt.services.interfaces.TokenService;
import com.schizoscrypt.storage.entities.RefreshTokenEntity;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.repositories.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

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

    @Override
    public String getTokenByUserEmail(String email) {

        Optional<RefreshTokenEntity> optionalToken = repository.findByEmail(email);

        if (optionalToken.isEmpty()) {
            throw new AppException("[ERROR] Token for user with email {" + email + "} doesn't exists", HttpStatus.NOT_FOUND);
        }

        String encryptedToken = optionalToken.get().getToken();
        String decryptedToken = encryptionService.decrypt(encryptedToken);

        return decryptedToken;
    }
}

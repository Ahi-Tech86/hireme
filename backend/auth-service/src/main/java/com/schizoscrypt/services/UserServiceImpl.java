package com.schizoscrypt.services;

import com.schizoscrypt.dtos.UserAccountDto;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.exception.AppException;
import com.schizoscrypt.factories.UserAccountDtoFactory;
import com.schizoscrypt.factories.UserDtoFactory;
import com.schizoscrypt.services.interfaces.UserService;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountDtoFactory factory;
    private final UserRepository repository;

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserAccountDto getByUserByEmail(String email) {

        UserEntity user;
        Optional<UserEntity> optionalUser = repository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new AppException("[ERROR] User with email {" + email + "} doesn't exists", HttpStatus.NOT_FOUND);
        } else {
            user = optionalUser.get();
        }

        UserAccountDto userDto = factory.makeUserAccountDto(user);

        return userDto;
    }
}

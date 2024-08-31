package com.schizoscrypt.services;

import com.schizoscrypt.services.interfaces.AuthService;
import lombok.extern.slf4j.Slf4j;
import com.schizoscrypt.dtos.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.storage.enums.AppRole;
import org.springframework.stereotype.Service;
import com.schizoscrypt.exception.AppException;
import com.schizoscrypt.dtos.CredentialRequest;
import com.schizoscrypt.factories.UserDtoFactory;
import com.schizoscrypt.factories.UserEntityFactory;
import com.schizoscrypt.storage.entities.UserEntity;
import com.schizoscrypt.storage.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository repository;
    private final UserEntityFactory factory;
    private final UserDtoFactory dtoFactory;
    private final TokenServiceImpl tokenService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto login(CredentialRequest request) {

        // getting user from db if user is exists
        UserEntity user = isUserExistsByEmail(request.getEmail());

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return dtoFactory.makeUserDto(user);
        } else {
            throw new AppException(
                    "[ERROR] Incorrect password for user with email {" + request.getEmail() + "}",
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {

        // checking email on uniqueness
        isEmailUniqueness(request.getEmail());

        // determine user role
        AppRole userRole = setAppRoleToUser(request.getRoleName());

        // mapping request to entity and encoding password
        UserEntity user = factory.makeUserEntity(request, userRole);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // create savedUser for return information about saved user in db. Using saveAndFlush instant changes in db
        UserEntity savedUser = repository.saveAndFlush(user);
        log.info("User with {} email was successfully saved", request.getEmail());

        // create and save refresh token in db
        tokenService.createAndSaveToken(user);

        return dtoFactory.makeUserDto(savedUser);
    }

    private AppRole setAppRoleToUser(String roleName) {

        AppRole userRole;
        String role = roleName.toLowerCase();

        if (role.equals("worker") || role.equals("employee")) {
            userRole = AppRole.WORKER_ROLE;
        } else if (role.equals("employer")) {
            userRole = AppRole.EMPLOYER_ROLE;
        } else {
            throw new AppException("[ERROR] ", HttpStatus.BAD_REQUEST);
        }

        return userRole;
    }

    private void isEmailUniqueness(String email) {

        Optional<UserEntity> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new AppException("[ERROR] User with email {" + email + "} is already exists", HttpStatus.BAD_REQUEST);
        }
    }

    private UserEntity isUserExistsByEmail(String email) {

        Optional<UserEntity> optionalUser = repository.findByEmail(email);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new AppException("[ERROR] User with email {" + email + "} doesn't exists", HttpStatus.NOT_FOUND);
        }
    }
}

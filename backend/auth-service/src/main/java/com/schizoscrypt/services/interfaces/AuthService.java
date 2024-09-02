package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.AuthenticatedUserDto;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.dtos.CredentialRequest;

import java.util.List;

public interface AuthService {
    List<Object> login(CredentialRequest request);

    UserDto register(RegisterRequest request);
}

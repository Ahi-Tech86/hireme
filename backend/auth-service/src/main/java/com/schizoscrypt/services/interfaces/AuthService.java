package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.dtos.CredentialRequest;

public interface AuthService {
    UserDto login(CredentialRequest request);

    UserDto register(RegisterRequest request);
}

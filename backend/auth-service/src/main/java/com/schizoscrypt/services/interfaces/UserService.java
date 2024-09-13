package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.UserAccountDto;
import com.schizoscrypt.dtos.UserDto;

public interface UserService {
    UserAccountDto getByUserByEmail(String email);
}

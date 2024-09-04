package com.schizoscrypt.services.interfaces;

import com.schizoscrypt.dtos.UserDto;

public interface UserService {
    UserDto getByUserByEmail(String email);
}

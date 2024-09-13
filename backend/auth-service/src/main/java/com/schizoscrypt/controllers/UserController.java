package com.schizoscrypt.controllers;

import com.schizoscrypt.dtos.EmailRequestDto;
import com.schizoscrypt.dtos.UserAccountDto;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.services.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl service;

    @GetMapping("/auth/getUser")
    public ResponseEntity<UserAccountDto> getUser(@RequestBody EmailRequestDto requestDto) {
        return ResponseEntity.ok(service.getByUserByEmail(requestDto.getEmail()));
    }
}

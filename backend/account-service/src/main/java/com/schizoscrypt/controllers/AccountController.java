package com.schizoscrypt.controllers;

import com.schizoscrypt.dtos.CreateEmployerAccountRequestDto;
import com.schizoscrypt.dtos.CreateWorkerAccountRequestDto;
import com.schizoscrypt.dtos.EmployerAccountDto;
import com.schizoscrypt.dtos.WorkerAccountDto;
import com.schizoscrypt.services.AccountServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImpl service;

    @GetMapping("/test")
    public String test() {
        return "Hello from account microservice";
    }

    @PostMapping("/worker/createAccount")
    public ResponseEntity<WorkerAccountDto> createWorkerAccount(
            HttpServletRequest request, @RequestBody CreateWorkerAccountRequestDto requestDto
    ) {

        Cookie[] cookies = request.getCookies();
        String cookieValue = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access-Token".equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }

        return ResponseEntity.ok(service.createWorkerAccount(cookieValue, requestDto));
    }

    @PostMapping("/employer/createAccount")
    public ResponseEntity<EmployerAccountDto> createEmployerAccount(
            HttpServletRequest request, @RequestBody CreateEmployerAccountRequestDto requestDto
    ) {
        Cookie[] cookies = request.getCookies();
        String cookieValue = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access-Token".equals(cookie.getName())) {
                    cookieValue = cookie.getValue();
                    break;
                }
            }
        }

        return ResponseEntity.ok(service.createEmployerAccount(cookieValue, requestDto));
    }
}
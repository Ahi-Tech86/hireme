package com.schizoscrypt.controllers;

import com.schizoscrypt.services.AccountServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountServiceImpl service;

    @GetMapping("/test")
    public String test() {
        return "Hello from account microservice";
    }

    @GetMapping("/mq")
    public ResponseEntity<String> mq(HttpServletRequest request) {

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

        return ResponseEntity.ok(service.createWorkerAccount(cookieValue));
    }
}
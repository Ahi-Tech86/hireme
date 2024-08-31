package com.schizoscrypt.controllers;

import com.schizoscrypt.dtos.CredentialRequest;
import com.schizoscrypt.dtos.RegisterRequest;
import com.schizoscrypt.dtos.UserDto;
import com.schizoscrypt.dtos.UserTokenDto;
import com.schizoscrypt.factories.UserTokenDtoFactory;
import com.schizoscrypt.services.AuthServiceImpl;
import com.schizoscrypt.services.JwtServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtServiceImpl jwtService;
    private final AuthServiceImpl authService;
    private final UserTokenDtoFactory factory;

    @PostMapping("/login")
    public ResponseEntity<UserTokenDto> login(HttpServletResponse response, @RequestBody CredentialRequest request) {

        UserDto user = authService.login(request);

        String accessToken = jwtService.generateAccessToken(request.getEmail());
        String refreshToken = jwtService.generateRefreshToken(request.getEmail());

        Cookie cookieAccessToken = new Cookie("access-Token", accessToken);
        cookieAccessToken.setHttpOnly(true);
        cookieAccessToken.setPath("/");
        cookieAccessToken.setMaxAge(24 * 60 * 60);

        Cookie cookieRefreshToken = new Cookie("jwt-refreshToken", refreshToken);
        cookieRefreshToken.setHttpOnly(true);
        cookieRefreshToken.setPath("/");
        cookieRefreshToken.setMaxAge(7 * 24 * 60 * 60);

        response.addCookie(cookieAccessToken);
        response.addCookie(cookieRefreshToken);

        UserTokenDto userTokenDto = factory.makeUserTokenDto(user, accessToken, refreshToken);

        return ResponseEntity.ok(userTokenDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registration(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/test")
    public String test() {
        return "Hello from secured endpoint";
    }
}

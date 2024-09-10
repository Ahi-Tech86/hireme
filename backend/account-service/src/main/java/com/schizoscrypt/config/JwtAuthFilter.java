package com.schizoscrypt.config;

import com.schizoscrypt.services.JwtServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtServiceImpl jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = "";

        // trying get accessToken from AUTHORIZATION header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] authElement = header.split(" ");
        if (authElement.length == 2 && "Bearer".equals(authElement[0])) {
            accessToken = authElement[1];
        }

        jwtService.validateAccessToken(accessToken);

    }
}

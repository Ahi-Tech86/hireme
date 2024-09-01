package com.schizoscrypt.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.ServletException;
import org.springframework.http.HttpHeaders;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthenticationProvider userAuthenticationProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String accessToken = null;

        // trying get accessToken from AUTHORIZATION header
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null) {
            String[] authElement = header.split(" ");
            if (authElement.length == 2 && "Bearer".equals(authElement[0])) {
                accessToken = authElement[1];
            }
        }

        //  trying get accessToken from Cookies
        if (accessToken == null) {
            Cookie cookie = WebUtils.getCookie(request, "access-Token");
            if (cookie != null) {
                accessToken = cookie.getValue();
            }
        }

        // token validation
        if (accessToken != null) {
            try {
                SecurityContextHolder.getContext().setAuthentication(
                        userAuthenticationProvider.validateAccessToken(accessToken)
                );
            } catch (RuntimeException exception) {
                SecurityContextHolder.clearContext();
                throw exception;
            }
        }

        filterChain.doFilter(request, response);
    }
}

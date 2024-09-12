package com.schizoscrypt.filters;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.schizoscrypt.filters.util.AppRole;
import com.schizoscrypt.filters.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CookieAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<CookieAuthorizationGatewayFilterFactory.Config> {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private RouteValidator validator;

    public static class Config {}

    public CookieAuthorizationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        /*
            Exchange is container which contains in self info about current request and response

            Chain is a design pattern that allows a request to be passed along a chain of handlers. Chain may include
                actions such as:
                    adding or editing headers
                    checking authentication adn authorization
                    logging requests
                    exception handling
         */
        return (exchange, chain) -> {

            if (validator.isSecured.test(exchange.getRequest())) {
                String accessToken = exchange.getRequest().getCookies().getFirst("access-Token") != null ?
                        exchange.getRequest().getCookies().getFirst("access-Token").getValue() : null;

                // check for cookie existence
                if (accessToken == null) {
                    return Mono.error(new RuntimeException("Access Token is missing"));
                }

                if (!jwtService.validateAccessToken(accessToken)) {
                    return Mono.error(new RuntimeException("Access token is invalid"));
                }

                if (jwtService.isAccessTokenExpired(accessToken)) {
                    String refreshToken = exchange
                            .getRequest().getCookies().getFirst("jwt-refreshToken") != null ?
                            exchange.getRequest().getCookies().getFirst("jwt-refreshToken").getValue() : null;

                    if (refreshToken == null) {
                        return Mono.error(new RuntimeException("Refresh token is missing"));
                    }

                    if (!jwtService.validateRefreshToken(refreshToken)) {
                        return Mono.error(new RuntimeException("Refresh token is invalid"));
                    }

                    if (jwtService.isRefreshTokenExpired(refreshToken)) {
                        return Mono.error(new RuntimeException("Refresh token is expired. Please log in again"));
                    }

                    String email = "";
                    AppRole role = jwtService.extractRoleFromRefreshToken(refreshToken);
                    String newAccessToken = jwtService.generateToken(email, role);

                    exchange.getResponse().getCookies()
                            .add("access-Token", ResponseCookie.from("access-Token", newAccessToken)
                                    .httpOnly(true)
                                    .path("/")
                                    .sameSite("Strict")
                                    .maxAge(24 * 60 * 60)
                                    .build()
                            );
                } else {
                    exchange = exchange.mutate()
                            .request(request -> request
                                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)).build();
                }
            }

            return chain.filter(exchange);
        };
    }
}

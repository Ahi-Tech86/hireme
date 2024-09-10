package com.schizoscrypt.filters;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.schizoscrypt.filters.util.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CookieAuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<CookieAuthorizationGatewayFilterFactory.Config> {

    @Autowired
    private JwtService jwtService;

    public static class Config {}

    public CookieAuthorizationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(CookieAuthorizationGatewayFilterFactory.Config config) {
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
            String accessToken = exchange.getRequest().getCookies().getFirst("access-Token") != null ?
                    exchange.getRequest().getCookies().getFirst("access-Token").getValue() : null;

            // check for cookie existence
            if (accessToken == null) {
                return Mono.error(new RuntimeException("Access Token is missing"));
            }

            exchange = exchange.mutate()
                    .request(request -> request
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)).build();

            return chain.filter(exchange);
        };
    }
}

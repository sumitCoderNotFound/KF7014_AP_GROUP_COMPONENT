package com.waterservices.apigateway.filters;

import com.waterservices.apigateway.security.TokenValidationResponse;
import com.waterservices.apigateway.security.TokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenValidator tokenValidator;

    private static final List<String> WHITELIST = List.of(
            "/api/authenticate",
            "/api/authenticate/login",
            "/api/authenticate/register",
            "/api/authenticate/refresh-token",
            "/api/authenticate/validate"
    );

    @Override
    public int getOrder() {
        return -1; // Run early
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        System.out.println("JwtAuthenticationFilter invoked for path: " + path);

        // Skip authentication for whitelisted paths
        if (WHITELIST.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        // Check Authorization header
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("Missing or invalid Authorization header");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Validate token
        String token = authHeader.substring(7);
        TokenValidationResponse validation = tokenValidator.validateToken(token);
        if (!validation.isValid()) {
            System.out.println("Token invalid: " + token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // Forward all headers + custom user header
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(builder -> builder.headers(headers -> {
                    exchange.getRequest().getHeaders().forEach(headers::put);         // copy all original headers
                    headers.set(HttpHeaders.AUTHORIZATION, authHeader);               // re-set token header explicitly
                    headers.set("X-User", validation.getUsername());                  // custom user header
                }))
                .build();

        return chain.filter(mutatedExchange);
    }
}
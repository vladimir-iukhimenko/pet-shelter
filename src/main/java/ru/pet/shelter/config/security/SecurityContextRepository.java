package ru.pet.shelter.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_PREFIX = "Bearer ";

    private AuthenticationManager authenticationManager;

    @Autowired
    public SecurityContextRepository(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        return null;
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        ServerHttpRequest request = serverWebExchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        String authToken = null;
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            authToken = authHeader.replace(TOKEN_PREFIX, "");
        } else {
            log.warn("Bearer header doesn't found, authorization isn't possible");
        }
        if (authToken != null) {
            Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
            return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
        } else {
            return Mono.empty();
        }
    }
}

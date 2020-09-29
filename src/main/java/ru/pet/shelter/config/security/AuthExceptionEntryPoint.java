package ru.pet.shelter.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;


public class AuthExceptionEntryPoint implements ServerAuthenticationEntryPoint {

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        final var exceptionResponse = new HashMap<String, Object>();

        exceptionResponse.put("error", "Unauthorized request");
        exceptionResponse.put("message", e.getLocalizedMessage());
        exceptionResponse.put("endpoint", exchange.getRequest().getPath());
        exceptionResponse.put("status", HttpStatus.UNAUTHORIZED);
        exceptionResponse.put("date", LocalDateTime.now());

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(exceptionResponse.toString().getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}

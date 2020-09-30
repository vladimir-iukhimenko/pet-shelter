package ru.pet.shelter.config.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.core.ResolvableType;
import org.springframework.core.codec.Hints;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;


public class AuthExceptionEntryPoint implements ServerAuthenticationEntryPoint {


    @SneakyThrows(JsonProcessingException.class)
    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        final var exceptionResponse = new HashMap<String, Object>();
        final var mapper = new ObjectMapper();

        exceptionResponse.put("error", "Unauthorized request");
        exceptionResponse.put("message", e.getLocalizedMessage());
        exceptionResponse.put("endpoint", exchange.getRequest().getPath().value());
        exceptionResponse.put("status", HttpStatus.UNAUTHORIZED);
        exceptionResponse.put("date", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));

        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(mapper.writeValueAsBytes(exceptionResponse));
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }
}

package ru.pet.shelter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;


@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public Mono<ResponseEntity<String>> handleAccessDenied(BadCredentialsException ex) {
        log.error(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR!!"));
    }
}

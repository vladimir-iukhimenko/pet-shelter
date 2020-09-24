package ru.pet.shelter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
@Slf4j
public class ExceptionHandler {

    public Mono<ResponseEntity<String>> handleAccessDenied(AccessDeniedException ex) {
        log.error(ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.FORBIDDEN).body("ERROR!!"));
    }
}

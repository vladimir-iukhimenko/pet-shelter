package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.service.PassportService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportRouter {

    private final PassportService passportService;
    private final Validator validator;

    @Autowired
    public PassportRouter(PassportService passportService, Validator validator) {
        this.passportService = passportService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/passport", beanClass = PassportService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/passport/{id}", beanClass = PassportService.class, beanMethod = "getById"),
            @RouterOperation(path = "/passport/save", beanClass = PassportService.class, beanMethod = "save"),
            @RouterOperation(path = "/passport/update/{id}", beanClass = PassportService.class, beanMethod = "update"),
            @RouterOperation(path = "/passport/delete/{id}", beanClass = PassportService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/passport/empty", beanClass = PassportService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> passportRoutes() {
        return RouterFunctions
                .route(GET("/passport").and(accept(MediaType.APPLICATION_JSON)), this::getAllPassports)
                .andRoute(GET("/passport/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getPassportById)
                .andRoute(POST("/passport/save").and(accept(MediaType.APPLICATION_JSON)), this::insertPassport)
                .andRoute(PUT("/passport/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updatePassport)
                .andRoute(DELETE("/passport/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deletePassport)
                .andRoute(GET("/passport/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyPassport);

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPassports(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(passportService.getAll(), Passport.class);
    }

    private Mono<ServerResponse> getPassportById(ServerRequest request) {
        return passportService.getById(request.pathVariable("id"))
                .flatMap(passport -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(passport))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPassport(ServerRequest request) {
        return request.bodyToMono(Passport.class)
                .doOnNext(this::validate)
                .flatMap(passport -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportService.save(passport), Passport.class));
    }

    private Mono<ServerResponse> updatePassport(ServerRequest request) {
        return request.bodyToMono(Passport.class)
                .doOnNext(this::validate)
                .flatMap(passport -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportService.save(passport), Passport.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePassport(ServerRequest request) {
        return passportService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPassport(ServerRequest request) {
        return ok().bodyValue(passportService.empty());
    }

    private void validate(Passport passport) {
        Errors errors = new BeanPropertyBindingResult(passport, "passport");
        validator.validate(passport, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.repository.PassportRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportRouter {

    private PassportRepository passportRepository;
    private Validator validator;

    @Autowired
    public PassportRouter(PassportRepository passportRepository, Validator validator) {
        this.passportRepository = passportRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/passport", beanClass = PassportRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/passport/{id}", beanClass = PassportRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> passportRoutes() {
        return
                route()
                        .GET("/passport", this::getAllPassports)

                        .GET("/passport/{id}", this::getPassportById)

                        .POST("/passport", this::insertPassport)

                        .PUT("/passport/{id}", this::updatePassport)

                        .DELETE("/passport/{id}", this::deletePassport)

                        .GET("/passport/empty", this::emptyPassport)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPassports(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(passportRepository.findAll(), Passport.class);
    }

    private Mono<ServerResponse> getPassportById(ServerRequest request) {
        return passportRepository.findById(request.pathVariable("id"))
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
                        .body(passportRepository.save(passport), Passport.class));
    }

    private Mono<ServerResponse> updatePassport(ServerRequest request) {
        return request.bodyToMono(Passport.class)
                .doOnNext(this::validate)
                .flatMap(passport -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportRepository.save(passport), Passport.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePassport(ServerRequest request) {
        return passportRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPassport(ServerRequest request) {
        return ok().bodyValue(new Passport());
    }

    private void validate(Passport passport) {
        Errors errors = new BeanPropertyBindingResult(passport, "passport");
        validator.validate(passport, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

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
import ru.pet.shelter.model.PassportVaccination;
import ru.pet.shelter.repository.PassportVaccinationRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportVaccinationRouter {

    private final PassportVaccinationRepository passportVaccinationRepository;
    private Validator validator;

    @Autowired
    public PassportVaccinationRouter(PassportVaccinationRepository passportVaccinationRepository, Validator validator) {
        this.passportVaccinationRepository = passportVaccinationRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/passport-vaccination", beanClass = PassportVaccinationRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/passport-vaccination/{id}", beanClass = PassportVaccination.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/passport-vaccination", this::getAllPassportVaccinations)

                        .GET("/passport-vaccination/{id}", this::getPassportVaccinationById)

                        .POST("/passport-vaccination", this::insertPassportVaccination)

                        .PUT("/passport-vaccination/{id}", this::updatePassportVaccination)

                        .DELETE("/passport-vaccination/{id}", this::deletePassportVaccination)

                        .GET("/passport-vaccination/empty", this::emptyPassportVaccination)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPassportVaccinations(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(passportVaccinationRepository.findAll(), PassportVaccination.class);
    }

    private Mono<ServerResponse> getPassportVaccinationById(ServerRequest request) {
        return passportVaccinationRepository.findById(request.pathVariable("id"))
                .flatMap(passportVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(passportVaccination))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPassportVaccination(ServerRequest request) {
        return request.bodyToMono(PassportVaccination.class)
                .doOnNext(this::validate)
                .flatMap(passportVaccination -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportVaccinationRepository.save(passportVaccination), PassportVaccination.class));
    }

    private Mono<ServerResponse> updatePassportVaccination(ServerRequest request) {
        return request.bodyToMono(PassportVaccination.class)
                .doOnNext(this::validate)
                .flatMap(passportVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportVaccinationRepository.save(passportVaccination), PassportVaccination.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePassportVaccination(ServerRequest request) {
        return passportVaccinationRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPassportVaccination(ServerRequest request) {
        return ok().bodyValue(new PassportVaccination());
    }

    private void validate(PassportVaccination passportVaccination) {
        Errors errors = new BeanPropertyBindingResult(passportVaccination, "passportVaccination");
        validator.validate(passportVaccination, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

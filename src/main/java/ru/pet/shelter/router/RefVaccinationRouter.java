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
import ru.pet.shelter.model.RefVaccination;
import ru.pet.shelter.repository.RefVaccinationRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefVaccinationRouter {

    private final RefVaccinationRepository refVaccinationRepository;
    private final Validator validator;

    @Autowired
    public RefVaccinationRouter(RefVaccinationRepository refVaccinationRepository, Validator validator) {
        this.refVaccinationRepository = refVaccinationRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-vaccination", beanClass = RefVaccinationRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-vaccination/{id}", beanClass = RefVaccinationRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/ref-vaccination", this::getAllRefVaccinations)

                        .GET("/ref-vaccination/{id}", this::getRefVaccinationById)

                        .POST("/ref-vaccination", this::insertRefVaccination)

                        .PUT("/ref-vaccination/{id}", this::updateRefVaccination)

                        .DELETE("/ref-vaccination/{id}", this::deleteRefVaccination)

                        .GET("/ref-vaccination/empty", this::emptyRefVaccination)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefVaccinations(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refVaccinationRepository.findAll(), RefVaccination.class);
    }

    private Mono<ServerResponse> getRefVaccinationById(ServerRequest request) {
        return refVaccinationRepository.findById(request.pathVariable("id"))
                .flatMap(refVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refVaccination))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefVaccination(ServerRequest request) {
        return request.bodyToMono(RefVaccination.class)
                .doOnNext(this::validate)
                .flatMap(refVaccination -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refVaccinationRepository.save(refVaccination), RefVaccination.class));
    }

    private Mono<ServerResponse> updateRefVaccination(ServerRequest request) {
        return request.bodyToMono(RefVaccination.class)
                .doOnNext(this::validate)
                .flatMap(refVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refVaccinationRepository.save(refVaccination), RefVaccination.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefVaccination(ServerRequest request) {
        return refVaccinationRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefVaccination(ServerRequest request) {
        return ok().bodyValue(new RefVaccination());
    }

    private void validate(RefVaccination refVaccination) {
        Errors errors = new BeanPropertyBindingResult(refVaccination, "refVaccination");
        validator.validate(refVaccination, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

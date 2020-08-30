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
import ru.pet.shelter.model.RefShelter;
import ru.pet.shelter.repository.RefShelterRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefShelterRouter {

    private final RefShelterRepository refShelterRepository;
    private final Validator validator;

    @Autowired
    public RefShelterRouter(RefShelterRepository refShelterRepository, Validator validator) {
        this.refShelterRepository = refShelterRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-shelter", beanClass = RefShelterRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-shelter/{id}", beanClass = RefShelterRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> refShelterRoutes() {
        return
                route()
                        .GET("/ref-shelter", this::getAllRefShelters)

                        .GET("/ref-shelter/{id}", this::getRefShelterById)

                        .POST("/ref-shelter", this::insertRefShelter)

                        .PUT("/ref-shelter/{id}", this::updateRefShelter)

                        .DELETE("/ref-shelter/{id}", this::deleteRefShelter)

                        .GET("/ref-shelter/empty", this::emptyRefShelter)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefShelters(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refShelterRepository.findAll(), RefShelter.class);
    }

    private Mono<ServerResponse> getRefShelterById(ServerRequest request) {
        return refShelterRepository.findById(request.pathVariable("id"))
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refShelter))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefShelter(ServerRequest request) {
        return request.bodyToMono(RefShelter.class)
                .doOnNext(this::validate)
                .flatMap(refShelter -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refShelterRepository.save(refShelter), RefShelter.class));
    }

    private Mono<ServerResponse> updateRefShelter(ServerRequest request) {
        return request.bodyToMono(RefShelter.class)
                .doOnNext(this::validate)
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refShelterRepository.save(refShelter), RefShelter.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefShelter(ServerRequest request) {
        return refShelterRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefShelter(ServerRequest request) {
        return ok().bodyValue(new RefShelter());
    }

    private void validate(RefShelter refShelter) {
        Errors errors = new BeanPropertyBindingResult(refShelter, "refShelter");
        validator.validate(refShelter, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

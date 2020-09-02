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
import ru.pet.shelter.model.RefShelter;
import ru.pet.shelter.service.RefShelterService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefShelterRouter {

    private final RefShelterService refShelterService;
    private final Validator validator;

    @Autowired
    public RefShelterRouter(RefShelterService refShelterService, Validator validator) {
        this.refShelterService = refShelterService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-Shelter", beanClass = RefShelterService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-Shelter/{id}", beanClass = RefShelterService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-Shelter/save", beanClass = RefShelterService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-Shelter/update/{id}", beanClass = RefShelterService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-Shelter/delete/{id}", beanClass = RefShelterService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-Shelter/empty", beanClass = RefShelterService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refShelterRoutes() {
        return RouterFunctions
                .route(GET("/ref-Shelter").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefShelters)
                .andRoute(GET("/ref-Shelter/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefShelterById)
                .andRoute(POST("/ref-Shelter/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefShelter)
                .andRoute(PUT("/ref-Shelter/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefShelter)
                .andRoute(DELETE("/ref-Shelter/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefShelter)
                .andRoute(GET("/ref-Shelter/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefShelter);
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefShelters(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refShelterService.getAll(), RefShelter.class);
    }

    private Mono<ServerResponse> getRefShelterById(ServerRequest request) {
        return refShelterService.getById(request.pathVariable("id"))
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
                        .body(refShelterService.save(refShelter), RefShelter.class));
    }

    private Mono<ServerResponse> updateRefShelter(ServerRequest request) {
        return request.bodyToMono(RefShelter.class)
                .doOnNext(this::validate)
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refShelterService.save(refShelter), RefShelter.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefShelter(ServerRequest request) {
        return refShelterService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefShelter(ServerRequest request) {
        return ok().bodyValue(refShelterService.empty());
    }

    private void validate(RefShelter refShelter) {
        Errors errors = new BeanPropertyBindingResult(refShelter, "refShelter");
        validator.validate(refShelter, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

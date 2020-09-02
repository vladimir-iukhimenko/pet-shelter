package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
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
import ru.pet.shelter.model.RefDogBreed;
import ru.pet.shelter.service.RefDogBreedService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefDogBreedRouter {

    private final RefDogBreedService refDogBreedService;
    private final Validator validator;

    public RefDogBreedRouter(RefDogBreedService refDogBreedService, Validator validator) {
        this.refDogBreedService = refDogBreedService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-DogBreed", beanClass = RefDogBreedService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-DogBreed/{id}", beanClass = RefDogBreedService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-DogBreed/save", beanClass = RefDogBreedService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-DogBreed/update/{id}", beanClass = RefDogBreedService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-DogBreed/delete/{id}", beanClass = RefDogBreedService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-DogBreed/empty", beanClass = RefDogBreedService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refDogBreedRoutes() {
        return RouterFunctions
                .route(GET("/ref-DogBreed").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefDogBreeds)
                .andRoute(GET("/ref-DogBreed/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefDogBreedById)
                .andRoute(POST("/ref-DogBreed/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefDogBreed)
                .andRoute(PUT("/ref-DogBreed/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefDogBreed)
                .andRoute(DELETE("/ref-DogBreed/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefDogBreed)
                .andRoute(GET("/ref-DogBreed/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefDogBreed);

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefDogBreeds(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refDogBreedService.getAll(), RefDogBreed.class);
    }

    private Mono<ServerResponse> getRefDogBreedById(ServerRequest request) {
        return refDogBreedService.getById(request.pathVariable("id"))
                .flatMap(refDogBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refDogBreed))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefDogBreed(ServerRequest request) {
        return request.bodyToMono(RefDogBreed.class)
                .doOnNext(this::validate)
                .flatMap(refDogBreed -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refDogBreedService.save(refDogBreed), RefDogBreed.class));
    }

    private Mono<ServerResponse> updateRefDogBreed(ServerRequest request) {
        return request.bodyToMono(RefDogBreed.class)
                .doOnNext(this::validate)
                .flatMap(refDogBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refDogBreedService.save(refDogBreed), RefDogBreed.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefDogBreed(ServerRequest request) {
        return refDogBreedService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefDogBreed(ServerRequest request) {
        return ok().bodyValue(refDogBreedService.empty());
    }

    private void validate(RefDogBreed refDogBreed) {
        Errors errors = new BeanPropertyBindingResult(refDogBreed, "refDogBreed");
        validator.validate(refDogBreed, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

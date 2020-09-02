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
import ru.pet.shelter.model.RefCatBreed;
import ru.pet.shelter.service.RefCatBreedService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefCatBreedRouter {

    private final RefCatBreedService refCatBreedService;
    private final Validator validator;

    @Autowired
    public RefCatBreedRouter(RefCatBreedService refCatBreedService, Validator validator) {
        this.refCatBreedService = refCatBreedService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-CatBreed", beanClass = RefCatBreedService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-CatBreed/{id}", beanClass = RefCatBreedService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-CatBreed/save", beanClass = RefCatBreedService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-CatBreed/update/{id}", beanClass = RefCatBreedService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-CatBreed/delete/{id}", beanClass = RefCatBreedService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-CatBreed/empty", beanClass = RefCatBreedService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refCatBreedRoutes() {
        return RouterFunctions
                .route(GET("/ref-CatBreed").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefCatBreeds)
                .andRoute(GET("/ref-CatBreed/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefCatBreedById)
                .andRoute(POST("/ref-CatBreed/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefCatBreed)
                .andRoute(PUT("/ref-CatBreed/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefCatBreed)
                .andRoute(DELETE("/ref-CatBreed/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefCatBreed)
                .andRoute(GET("/ref-CatBreed/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefCatBreed);
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefCatBreeds(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refCatBreedService.getAll(), RefCatBreed.class);
    }

    private Mono<ServerResponse> getRefCatBreedById(ServerRequest request) {
        return refCatBreedService.getById(request.pathVariable("id"))
                .flatMap(refCatBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refCatBreed))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefCatBreed(ServerRequest request) {
        return request.bodyToMono(RefCatBreed.class)
                .doOnNext(this::validate)
                .flatMap(refCatBreed -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refCatBreedService.save(refCatBreed), RefCatBreed.class));
    }

    private Mono<ServerResponse> updateRefCatBreed(ServerRequest request) {
        return request.bodyToMono(RefCatBreed.class)
                .doOnNext(this::validate)
                .flatMap(refCatBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refCatBreedService.save(refCatBreed), RefCatBreed.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefCatBreed(ServerRequest request) {
        return refCatBreedService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefCatBreed(ServerRequest request) {
        return ok().bodyValue(refCatBreedService.empty());
    }

    private void validate(RefCatBreed refCatBreed) {
        Errors errors = new BeanPropertyBindingResult(refCatBreed, "refСatBreed");
        validator.validate(refCatBreed, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

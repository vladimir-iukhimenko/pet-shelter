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
import ru.pet.shelter.model.RefCatBreed;
import ru.pet.shelter.repository.RefCatBreedRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefCatBreedRouter {

    private final RefCatBreedRepository refCatBreedRepository;
    private final Validator validator;

    @Autowired
    public RefCatBreedRouter(RefCatBreedRepository refCatBreedRepository, Validator validator) {
        this.refCatBreedRepository = refCatBreedRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-catBreed", beanClass = RefCatBreedRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-catBreed/{id}", beanClass = RefCatBreedRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> refCatBreedRoutes() {
        return
                route()
                        .GET("/ref-catBreed", this::getAllRefCatBreeds)

                        .GET("/ref-catBreed/{id}", this::getRefCatBreedById)

                        .POST("/ref-catBreed", this::insertRefCatBreed)

                        .PUT("/ref-catBreed/{id}", this::updateRefCatBreed)

                        .DELETE("/ref-catBreed/{id}", this::deleteRefCatBreed)

                        .GET("/ref-catBreed/empty", this::emptyRefCatBreed)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefCatBreeds(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refCatBreedRepository.findAll(), RefCatBreed.class);
    }

    private Mono<ServerResponse> getRefCatBreedById(ServerRequest request) {
        return refCatBreedRepository.findById(request.pathVariable("id"))
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
                        .body(refCatBreedRepository.save(refCatBreed), RefCatBreed.class));
    }

    private Mono<ServerResponse> updateRefCatBreed(ServerRequest request) {
        return request.bodyToMono(RefCatBreed.class)
                .doOnNext(this::validate)
                .flatMap(refCatBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refCatBreedRepository.save(refCatBreed), RefCatBreed.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefCatBreed(ServerRequest request) {
        return refCatBreedRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefCatBreed(ServerRequest request) {
        return ok().bodyValue(new RefCatBreed());
    }

    private void validate(RefCatBreed refCatBreed) {
        Errors errors = new BeanPropertyBindingResult(refCatBreed, "refСatBreed");
        validator.validate(refCatBreed, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

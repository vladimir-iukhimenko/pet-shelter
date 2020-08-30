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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefDogBreed;
import ru.pet.shelter.repository.RefDogBreedRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefDogBreedRouter {

    private final RefDogBreedRepository refDogBreedRepository;
    private final Validator validator;

    public RefDogBreedRouter(RefDogBreedRepository refDogBreedRepository, Validator validator) {
        this.refDogBreedRepository = refDogBreedRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-dogBreed", beanClass = RefDogBreedRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-dogBreed/{id}", beanClass = RefDogBreedRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/ref-dogBreed", this::getAllRefDogBreeds)

                        .GET("/ref-dogBreed/{id}", this::getRefDogBreedById)

                        .POST("/ref-dogBreed", this::insertRefDogBreed)

                        .PUT("/ref-dogBreed/{id}", this::updateRefDogBreed)

                        .DELETE("/ref-dogBreed/{id}", this::deleteRefDogBreed)

                        .GET("/ref-dogBreed/empty", this::emptyRefDogBreed)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefDogBreeds(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refDogBreedRepository.findAll(), RefDogBreed.class);
    }

    private Mono<ServerResponse> getRefDogBreedById(ServerRequest request) {
        return refDogBreedRepository.findById(request.pathVariable("id"))
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
                        .body(refDogBreedRepository.save(refDogBreed), RefDogBreed.class));
    }

    private Mono<ServerResponse> updateRefDogBreed(ServerRequest request) {
        return request.bodyToMono(RefDogBreed.class)
                .doOnNext(this::validate)
                .flatMap(refDogBreed -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refDogBreedRepository.save(refDogBreed), RefDogBreed.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefDogBreed(ServerRequest request) {
        return refDogBreedRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefDogBreed(ServerRequest request) {
        return ok().bodyValue(new RefDogBreed());
    }

    private void validate(RefDogBreed refDogBreed) {
        Errors errors = new BeanPropertyBindingResult(refDogBreed, "refDogBreed");
        validator.validate(refDogBreed, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

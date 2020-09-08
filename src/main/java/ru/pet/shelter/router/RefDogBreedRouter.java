package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefDogBreed;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefDogBreedService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefDogBreedRouter {

    private final RefDogBreedService refDogBreedService;
    private final EntityValidator<RefDogBreed> validator;

    public RefDogBreedRouter(RefDogBreedService refDogBreedService, EntityValidator<RefDogBreed> validator) {
        this.refDogBreedService = refDogBreedService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/dog-breed", beanClass = RefDogBreedService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/dog-breed/{id}", beanClass = RefDogBreedService.class, beanMethod = "getById"),
            @RouterOperation(path = "/dog-breed/save", beanClass = RefDogBreedService.class, beanMethod = "save"),
            @RouterOperation(path = "/dog-breed/update/{id}", beanClass = RefDogBreedService.class, beanMethod = "update"),
            @RouterOperation(path = "/dog-breed/delete/{id}", beanClass = RefDogBreedService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/dog-breed/empty", beanClass = RefDogBreedService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refDogBreedRoutes() {
        return
                route()
                        .GET("/dog-breed", this::getAllRefDogBreeds)

                        .GET("/dog-breed/{id}", this::getRefDogBreedById)

                        .POST("/dog-breed/save", this::insertRefDogBreed)

                        .PUT("/dog-breed/update/{id}", this::updateRefDogBreed)

                        .DELETE("/dog-breed/delete/{id}", this::deleteRefDogBreed)

                        .GET("/dog-breed/empty", this::emptyRefDogBreed)

                        .build();
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
                .doOnNext(validator::validate)
                .flatMap(refDogBreed -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refDogBreedService.save(refDogBreed), RefDogBreed.class));
    }

    private Mono<ServerResponse> updateRefDogBreed(ServerRequest request) {
        return request.bodyToMono(RefDogBreed.class)
                .doOnNext(validator::validate)
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
}

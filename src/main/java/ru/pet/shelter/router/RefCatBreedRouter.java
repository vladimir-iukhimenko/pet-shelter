package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefCatBreed;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefCatBreedService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefCatBreedRouter {

    private final RefCatBreedService refCatBreedService;
    private final EntityValidator<RefCatBreed> validator;

    @Autowired
    public RefCatBreedRouter(RefCatBreedService refCatBreedService, EntityValidator<RefCatBreed> validator) {
        this.refCatBreedService = refCatBreedService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/cat-breed", beanClass = RefCatBreedService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/cat-breed/{id}", beanClass = RefCatBreedService.class, beanMethod = "getById"),
            @RouterOperation(path = "/cat-breed/save", beanClass = RefCatBreedService.class, beanMethod = "save"),
            @RouterOperation(path = "/cat-breed/update/{id}", beanClass = RefCatBreedService.class, beanMethod = "update"),
            @RouterOperation(path = "/cat-breed/delete/{id}", beanClass = RefCatBreedService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/cat-breed/empty", beanClass = RefCatBreedService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refCatBreedRoutes() {
        return
                route()
                        .GET("/cat-breed", this::getAllRefCatBreeds)

                        .GET("/cat-breed/{id}", this::getRefCatBreedById)

                        .POST("/cat-breed/save", this::insertRefCatBreed)

                        .PUT("/cat-breed/update/{id}", this::updateRefCatBreed)

                        .DELETE("/cat-breed/delete/{id}", this::deleteRefCatBreed)

                        .GET("/cat-breed/empty", this::emptyRefCatBreed)

                        .build();
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
                .doOnNext(validator::validate)
                .flatMap(refCatBreed -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refCatBreedService.save(refCatBreed), RefCatBreed.class));
    }

    private Mono<ServerResponse> updateRefCatBreed(ServerRequest request) {
        return request.bodyToMono(RefCatBreed.class)
                .doOnNext(validator::validate)
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
}

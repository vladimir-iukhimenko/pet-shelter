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
import ru.pet.shelter.model.Shelter;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefShelterService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefShelterRouter {

    private final RefShelterService refShelterService;
    private final EntityValidator<Shelter> validator;

    @Autowired
    public RefShelterRouter(RefShelterService refShelterService, EntityValidator<Shelter> validator) {
        this.refShelterService = refShelterService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/shelter", beanClass = RefShelterService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/shelter/{id}", beanClass = RefShelterService.class, beanMethod = "getById"),
            @RouterOperation(path = "/shelter/save", beanClass = RefShelterService.class, beanMethod = "save"),
            @RouterOperation(path = "/shelter/update/{id}", beanClass = RefShelterService.class, beanMethod = "update"),
            @RouterOperation(path = "/shelter/delete/{id}", beanClass = RefShelterService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/shelter/empty", beanClass = RefShelterService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refShelterRoutes() {
        return
                route()
                        .GET("/shelter", this::getAllRefShelters)

                        .GET("/shelter/{id}", this::getRefShelterById)

                        .POST("/shelter/save", this::insertRefShelter)

                        .PUT("/shelter/update/{id}", this::updateRefShelter)

                        .DELETE("/shelter/delete/{id}", this::deleteRefShelter)

                        .GET("/shelter/empty", this::emptyRefShelter)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefShelters(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refShelterService.getAll(), Shelter.class);
    }

    private Mono<ServerResponse> getRefShelterById(ServerRequest request) {
        return refShelterService.getById(request.pathVariable("id"))
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refShelter))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefShelter(ServerRequest request) {
        return request.bodyToMono(Shelter.class)
                .doOnNext(validator::validate)
                .flatMap(refShelter -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refShelterService.save(refShelter), Shelter.class));
    }

    private Mono<ServerResponse> updateRefShelter(ServerRequest request) {
        return request.bodyToMono(Shelter.class)
                .doOnNext(validator::validate)
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refShelterService.save(refShelter), Shelter.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefShelter(ServerRequest request) {
        return refShelterService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefShelter(ServerRequest request) {
        return ok().bodyValue(refShelterService.empty());
    }
}

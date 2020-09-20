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
import ru.pet.shelter.service.ShelterPetService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class ShelterRouter {

    private final ShelterPetService shelterService;
    private final EntityValidator<Shelter> validator;

    @Autowired
    public ShelterRouter(ShelterPetService shelterService, EntityValidator<Shelter> validator) {
        this.shelterService = shelterService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/shelter", beanClass = ShelterPetService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/shelter/{id}", beanClass = ShelterPetService.class, beanMethod = "getById"),
            @RouterOperation(path = "/shelter/save", beanClass = ShelterPetService.class, beanMethod = "save"),
            @RouterOperation(path = "/shelter/update/{id}", beanClass = ShelterPetService.class, beanMethod = "update"),
            @RouterOperation(path = "/shelter/delete/{id}", beanClass = ShelterPetService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/shelter/empty", beanClass = ShelterPetService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refShelterRoutes() {
        return
                route()
                        .GET("/shelter", this::getAllShelters)

                        .GET("/shelter/empty", this::emptyShelter)

                        .GET("/shelter/{id}", this::getShelterById)

                        .POST("/shelter/save", this::insertShelter)

                        .PUT("/shelter/update/{id}", this::updateShelter)

                        .DELETE("/shelter/delete/{id}", this::deleteShelter)


                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllShelters(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(shelterService.getAll(), Shelter.class);
    }

    private Mono<ServerResponse> getShelterById(ServerRequest request) {
        return shelterService.getById(request.pathVariable("id"))
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refShelter))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertShelter(ServerRequest request) {
        return request.bodyToMono(Shelter.class)
                .doOnNext(validator::validate)
                .flatMap(refShelter -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(shelterService.save(refShelter), Shelter.class));
    }

    private Mono<ServerResponse> updateShelter(ServerRequest request) {
        return request.bodyToMono(Shelter.class)
                .doOnNext(validator::validate)
                .flatMap(refShelter -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(shelterService.save(refShelter), Shelter.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteShelter(ServerRequest request) {
        return shelterService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyShelter(ServerRequest request) {
        return ok().body(shelterService.empty(), Shelter.class);
    }
}

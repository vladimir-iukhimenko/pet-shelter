package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.model.view.PetView;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.DogService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class DogRouter {

    private final DogService dogService;
    private final EntityValidator<Dog> validator;

    @Autowired
    public DogRouter(DogService dogService, EntityValidator<Dog> validator) {
        this.dogService = dogService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/dog", beanClass = DogService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/dog/{id}", beanClass = DogService.class, beanMethod = "getById"),
            @RouterOperation(path = "/dog/save", beanClass = DogService.class, beanMethod = "save"),
            @RouterOperation(path = "/dog/update/{id}", beanClass = DogService.class, beanMethod = "update"),
            @RouterOperation(path = "/dog/delete/{id}", beanClass = DogService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/dog/empty", beanClass = DogService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> dogRoutes() {
        return
                route()
                        .GET("/dog", this::getAllDogs)

                        .GET("/dog/empty", this::emptyDog)

                        .GET("/dog/{id}", this::getDogById)

                        .POST("/dog/save", this::insertDog)

                        .PUT("/dog/update/{id}", this::updateDog)

                        .DELETE("/dog/delete/{id}", this::deleteDog)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllDogs(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).hint(Jackson2CodecSupport.JSON_VIEW_HINT, PetView.REST.class).body(dogService.getAll(), Dog.class);
    }

    private Mono<ServerResponse> getDogById(ServerRequest request) {
        return dogService.getById(request.pathVariable("id"))
                .flatMap(dog -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, PetView.INTERNAL.class)
                        .bodyValue(dog))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertDog(ServerRequest request) {
        return request.bodyToMono(Dog.class)
                .doOnNext(validator::validate)
                .flatMap(dog -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dogService.save(dog), Dog.class));
    }

    private Mono<ServerResponse> updateDog(ServerRequest request) {
        return request.bodyToMono(Dog.class)
                .doOnNext(validator::validate)
                .flatMap(dog -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dogService.save(dog), Dog.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteDog(ServerRequest request) {
        return dogService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyDog(ServerRequest request) {
        return ok().body(dogService.empty(), Dog.class);
    }

}

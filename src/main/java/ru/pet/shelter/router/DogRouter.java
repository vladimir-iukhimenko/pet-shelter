package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.web.bind.annotation.RequestMethod;
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
            @RouterOperation(path = "/dog", method = RequestMethod.GET, beanClass = DogService.class, beanMethod = "findAll"),
            @RouterOperation(path = "/dog/empty", method = RequestMethod.GET, beanClass = DogService.class, beanMethod = "empty"),
            @RouterOperation(path = "/dog/{id}", method = RequestMethod.GET, beanClass = DogService.class, beanMethod = "findById"),
            @RouterOperation(path = "/dog", method = RequestMethod.POST, beanClass = DogService.class, beanMethod = "save"),
            @RouterOperation(path = "/dog", method = RequestMethod.PUT, beanClass = DogService.class, beanMethod = "update"),
            @RouterOperation(path = "/dog/{id}", method = RequestMethod.DELETE, beanClass = DogService.class, beanMethod = "removeById")

    })
    RouterFunction<ServerResponse> dogRoutes() {
        return
                route()
                        .GET("/dog", this::getAllDogs)

                        .GET("/dog/empty", this::emptyDog)

                        .GET("/dog/{id}", this::getDogById)

                        .POST("/dog", this::insertDog)

                        .PUT("/dog", this::updateDog)

                        .DELETE("/dog/{id}", this::deleteDog)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllDogs(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).hint(Jackson2CodecSupport.JSON_VIEW_HINT, PetView.REST.class).body(dogService.findAll(), Dog.class);
    }

    private Mono<ServerResponse> getDogById(ServerRequest request) {
        return dogService.findById(request.pathVariable("id"))
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
        return dogService.removeById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyDog(ServerRequest request) {
        return ok().body(dogService.empty(), Dog.class);
    }

}

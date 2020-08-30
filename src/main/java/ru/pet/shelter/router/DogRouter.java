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
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.repository.DogRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class DogRouter {

    private DogRepository dogRepository;
    private Validator validator;

    @Autowired
    public DogRouter(DogRepository dogRepository, Validator validator) {
        this.dogRepository = dogRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/dog", beanClass = DogRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/dog/{id}", beanClass = DogRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> dogRoutes() {
        return
                route()
                        .GET("/dog", this::getAllDogs)

                        .GET("/dog/{id}", this::getDogById)

                        .POST("/dog", this::insertDog)

                        .PUT("/dog/{id}", this::updateDog)

                        .DELETE("/dog/{id}", this::deleteDog)

                        .GET("/dog/empty", this::emptyDog)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllDogs(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(dogRepository.findAll(), Dog.class);
    }

    private Mono<ServerResponse> getDogById(ServerRequest request) {
        return dogRepository.findById(request.pathVariable("id"))
                .flatMap(dog -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dog))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertDog(ServerRequest request) {
        return request.bodyToMono(Dog.class)
                .doOnNext(this::validate)
                .flatMap(dog -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dogRepository.save(dog), Dog.class));
    }

    private Mono<ServerResponse> updateDog(ServerRequest request) {
        return request.bodyToMono(Dog.class)
                .doOnNext(this::validate)
                .flatMap(dog -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(dogRepository.save(dog), Dog.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteDog(ServerRequest request) {
        return dogRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyDog(ServerRequest request) {
        return ok().bodyValue(new Dog());
    }

    private void validate(Dog dog) {
        Errors errors = new BeanPropertyBindingResult(dog, "dog");
        validator.validate(dog, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

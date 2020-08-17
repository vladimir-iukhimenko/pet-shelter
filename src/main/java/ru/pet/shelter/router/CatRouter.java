package ru.pet.shelter.router;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.http.HttpStatus.*;

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
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.repository.CatRepository;


@Configuration
public class CatRouter {

    @Autowired
    private CatRepository catRepository;

    @Autowired
    private Validator validator;

    @Bean
    @RouterOperations({@RouterOperation(path = "/cat", beanClass = CatRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/cat/{id}", beanClass = CatRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> catRoutes() {
        return
                route()
                        .GET("/cat", this::getAllCats)

                        .GET("/cat/{id}", this::getCatById)

                        .POST("/cat", this::insertCat)

                        .PUT("/cat/{id}", this::updateCat)

                        .DELETE("/cat/{id}", this::deleteCat)

                        .GET("/cat/empty", this::emptyCat)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllCats(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(catRepository.findAll(), Cat.class);
    }

    private Mono<ServerResponse> getCatById(ServerRequest request) {
        return catRepository.findById(request.pathVariable("id"))
                .flatMap(cat -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(cat))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertCat(ServerRequest request) {
        return request.bodyToMono(Cat.class)
                .doOnNext(this::validate)
                .flatMap(cat -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(catRepository.save(cat), Cat.class));
    }


    private Mono<ServerResponse> updateCat(ServerRequest request) {
        return request.bodyToMono(Cat.class)
                .doOnNext(this::validate)
                .flatMap(cat -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(catRepository.save(cat), Cat.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteCat(ServerRequest request) {
        return catRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyCat(ServerRequest request) {
        return ok().bodyValue(new Cat());
    }

    private void validate(Cat cat) {
        Errors errors = new BeanPropertyBindingResult(cat, "cat");
        validator.validate(cat, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

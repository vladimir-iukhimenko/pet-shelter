package ru.pet.shelter.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
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
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.service.CatService;


@Configuration
public class CatRouter {

    private final Validator validator;
    private final CatService catService;

    @Autowired
    public CatRouter(CatService catService, Validator validator) {
        this.validator = validator;
        this.catService = catService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/cat", beanClass = CatService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/cat/{id}", beanClass = CatService.class, beanMethod = "getById"),
            @RouterOperation(path = "/cat/save", beanClass = CatService.class, beanMethod = "save"),
            @RouterOperation(path = "/cat/update/{id}", beanClass = CatService.class, beanMethod = "update"),
            @RouterOperation(path = "/cat/delete/{id}", beanClass = CatService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/cat/empty", beanClass = CatService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> catRoutes() {
        return RouterFunctions
                .route(GET("/cat").and(accept(MediaType.APPLICATION_JSON)), this::getAllCats)
                .andRoute(GET("/cat/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getCatById)
                .andRoute(POST("/cat/save").and(accept(MediaType.APPLICATION_JSON)), this::insertCat)
                .andRoute(PUT("/cat/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateCat)
                .andRoute(DELETE("/cat/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteCat)
                .andRoute(GET("/cat/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyCat);
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();


    private Mono<ServerResponse> getAllCats(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(catService.getAll(), Cat.class);
    }

    private Mono<ServerResponse> getCatById(ServerRequest request) {
        return catService.getById(request.pathVariable("id"))
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
                        .body(catService.save(cat), Cat.class));
    }


    private Mono<ServerResponse> updateCat(ServerRequest request) {
        return request.bodyToMono(Cat.class)
                .doOnNext(this::validate)
                .flatMap(cat -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(catService.save(cat), Cat.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteCat(ServerRequest request) {
        return catService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyCat(ServerRequest request) {
        return ok().bodyValue(catService.empty());
    }

    private void validate(Cat cat) {
        Errors errors = new BeanPropertyBindingResult(cat, "cat");
        validator.validate(cat, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

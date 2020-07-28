package ru.pet.shelter.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.repository.CatRepository;


@Configuration
public class CatRouter {

    private CatRepository catRepository;

    @Autowired
    public void setCatRepository(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Bean
    @Transactional
    RouterFunction<ServerResponse> catCrudRoutes() {
        return
                route(GET("/cat"),
                        serverRequest -> ok().body(catRepository.findAll(), Cat.class))
                .and(
                        route(GET("/cat/{id}"),
                                serverRequest -> ok().body(catRepository.findById(serverRequest.pathVariable("id")), Cat.class)))
                .and(
                        route(POST("/cat/"),
                                serverRequest -> serverRequest.bodyToMono(Cat.class)
                                        .doOnNext(catRepository::insert)
                                        .then(ok().build())))
                .and(
                        route(PUT("/cat/{id}"),
                                serverRequest -> serverRequest.bodyToMono(Cat.class)
                                        .doOnNext(catRepository::save)
                                        .then(ok().build()))
                )
                .and(
                        route(DELETE("/cat/{id}"),
                                serverRequest -> catRepository.deleteById(serverRequest.pathVariable("id"))
                                        .then(status(HttpStatus.NOT_FOUND).build()))
                );
    }
}

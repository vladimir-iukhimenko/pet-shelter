package ru.pet.shelter.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.repository.ChipRepository;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

@Configuration
public class ChipRouter {

    @Autowired
    private ChipRepository chipRepository;

    @Bean
    @Transactional
    RouterFunction<ServerResponse> chipCrudRoutes() {
        return
                route(GET("/chip"),
                        serverRequest -> ok().body(chipRepository.findAll(), Chip.class))
                        .and(
                                route(GET("/chip/{id}"),
                                        serverRequest -> ok().body(chipRepository.findById(serverRequest.pathVariable("id")), Chip.class)))
                        .and(
                                route(POST("/chip/"),
                                        serverRequest -> serverRequest.bodyToMono(Chip.class)
                                                .doOnNext(chipRepository::save)
                                                .then(ok().build())))
                        .and(
                                route(PUT("/chip/{id}"),
                                        serverRequest -> serverRequest.bodyToMono(Chip.class)
                                                .doOnNext(chipRepository::save)
                                                .then(ok().build()))
                        )
                        .and(
                                route(DELETE("/chip/{id}"),
                                        serverRequest -> chipRepository.deleteById(serverRequest.pathVariable("id"))
                                                .then(status(HttpStatus.NOT_FOUND).build()))
                        );
    }
}

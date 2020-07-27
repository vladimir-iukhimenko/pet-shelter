package ru.pet.shelter.router;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    RouterFunction<ServerResponse> CrudRoutes() {
        return
                route(GET("/cats"), serverRequest -> ok().body(catRepository::findAllCats, Cat.class))
                .and(route(GET("/cats/{id}"), serverRequest -> ok().body(catRepository.findCatById(serverRequest.pathVariable("id")), Cat.class)));
    }
}

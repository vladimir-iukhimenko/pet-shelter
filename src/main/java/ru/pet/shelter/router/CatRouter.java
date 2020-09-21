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
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.view.PetView;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.CatService;


@Configuration
public class CatRouter {

    private final EntityValidator<Cat> validator;
    private final CatService catService;

    @Autowired
    public CatRouter(CatService catService, EntityValidator<Cat> validator) {
        this.validator = validator;
        this.catService = catService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/cat", method = RequestMethod.GET, beanClass = CatService.class, beanMethod = "findAll"),
            @RouterOperation(path = "/cat/empty", method = RequestMethod.GET, beanClass = CatService.class, beanMethod = "empty"),
            @RouterOperation(path = "/cat/{id}", method = RequestMethod.GET, beanClass = CatService.class, beanMethod = "findById"),
            @RouterOperation(path = "/cat", method = RequestMethod.POST, beanClass = CatService.class, beanMethod = "save"),
            @RouterOperation(path = "/cat", method = RequestMethod.PUT, beanClass = CatService.class, beanMethod = "update"),
            @RouterOperation(path = "/cat/{id}", method = RequestMethod.DELETE, beanClass = CatService.class, beanMethod = "removeById")

    })
    RouterFunction<ServerResponse> catRoutes() {
        return
                route()
                        .GET("/cat", this::getAllCats)

                        .GET("/cat/empty", this::emptyCat)

                        .GET("/cat/{id}", this::getCatById)

                        .POST("/cat", this::insertCat)

                        .PUT("/cat", this::updateCat)

                        .DELETE("/cat/{id}", this::deleteCat)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();


    private Mono<ServerResponse> getAllCats(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).hint(Jackson2CodecSupport.JSON_VIEW_HINT, PetView.REST.class).body(catService.findAll(), Cat.class);
    }

    private Mono<ServerResponse> getCatById(ServerRequest request) {
        return catService.findById(request.pathVariable("id"))
                .flatMap(cat -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .hint(Jackson2CodecSupport.JSON_VIEW_HINT, PetView.INTERNAL.class)
                        .bodyValue(cat))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertCat(ServerRequest request) {
        return request.bodyToMono(Cat.class)
                .doOnNext(validator::validate)
                .flatMap(cat -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(catService.save(cat), Cat.class));
    }


    private Mono<ServerResponse> updateCat(ServerRequest request) {
        return request.bodyToMono(Cat.class)
                .doOnNext(validator::validate)
                .flatMap(cat -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(catService.save(cat), Cat.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteCat(ServerRequest request) {
        return catService.removeById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyCat(ServerRequest request) {
        return ok().body(catService.empty(), Cat.class);
    }

}

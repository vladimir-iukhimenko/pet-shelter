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
import ru.pet.shelter.model.RefFurColor;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefFurColorService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefFurColorRouter {

    private final RefFurColorService refFurColorService;
    private final EntityValidator<RefFurColor> validator;

    @Autowired
    public RefFurColorRouter(RefFurColorService refFurColorService, EntityValidator<RefFurColor> validator) {
        this.refFurColorService = refFurColorService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/fur-color", beanClass = RefFurColorService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/fur-color/{id}", beanClass = RefFurColorService.class, beanMethod = "getById"),
            @RouterOperation(path = "/fur-color/save", beanClass = RefFurColorService.class, beanMethod = "save"),
            @RouterOperation(path = "/fur-color/update/{id}", beanClass = RefFurColorService.class, beanMethod = "update"),
            @RouterOperation(path = "/fur-color/delete/{id}", beanClass = RefFurColorService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/fur-color/empty", beanClass = RefFurColorService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refFurColorRoutes() {
        return
                route()
                        .GET("/fur-color", this::getAllRefFurColors)

                        .GET("/fur-color/{id}", this::getRefFurColorById)

                        .POST("/fur-color/save", this::insertRefFurColor)

                        .PUT("/fur-color/update/{id}", this::updateRefFurColor)

                        .DELETE("/fur-color/delete/{id}", this::deleteRefFurColor)

                        .GET("/fur-color/empty", this::emptyRefFurColor)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefFurColors(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refFurColorService.getAll(), RefFurColor.class);
    }

    private Mono<ServerResponse> getRefFurColorById(ServerRequest request) {
        return refFurColorService.getById(request.pathVariable("id"))
                .flatMap(refFurColor -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refFurColor))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefFurColor(ServerRequest request) {
        return request.bodyToMono(RefFurColor.class)
                .doOnNext(validator::validate)
                .flatMap(refFurColor -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurColorService.save(refFurColor), RefFurColor.class));
    }

    private Mono<ServerResponse> updateRefFurColor(ServerRequest request) {
        return request.bodyToMono(RefFurColor.class)
                .doOnNext(validator::validate)
                .flatMap(refFurColor -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurColorService.save(refFurColor), RefFurColor.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefFurColor(ServerRequest request) {
        return refFurColorService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefFurColor(ServerRequest request) {
        return ok().bodyValue(refFurColorService.empty());
    }
}

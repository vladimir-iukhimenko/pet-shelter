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
import ru.pet.shelter.model.RefFur;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefFurService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Configuration
public class RefFurRouter {

    private final RefFurService refFurService;
    private final EntityValidator<RefFur> validator;

    @Autowired
    public RefFurRouter(RefFurService refFurService, EntityValidator<RefFur> validator) {
        this.refFurService = refFurService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/fur", beanClass = RefFurService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/fur/{id}", beanClass = RefFurService.class, beanMethod = "getById"),
            @RouterOperation(path = "/fur/save", beanClass = RefFurService.class, beanMethod = "save"),
            @RouterOperation(path = "/fur/update/{id}", beanClass = RefFurService.class, beanMethod = "update"),
            @RouterOperation(path = "/fur/delete/{id}", beanClass = RefFurService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/fur/empty", beanClass = RefFurService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refFurRoutes() {
        return
                route()
                        .GET("/fur", this::getAllRefFurs)

                        .GET("/fur/{id}", this::getRefFurById)

                        .POST("/fur/save", this::insertRefFur)

                        .PUT("/fur/update/{id}", this::updateRefFur)

                        .DELETE("/fur/delete/{id}", this::deleteRefFur)

                        .GET("/fur/empty", this::emptyRefFur)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefFurs(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refFurService.getAll(), RefFur.class);
    }

    private Mono<ServerResponse> getRefFurById(ServerRequest request) {
        return refFurService.getById(request.pathVariable("id"))
                .flatMap(refFur -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refFur))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefFur(ServerRequest request) {
        return request.bodyToMono(RefFur.class)
                .doOnNext(validator::validate)
                .flatMap(refFur -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurService.save(refFur), RefFur.class));
    }

    private Mono<ServerResponse> updateRefFur(ServerRequest request) {
        return request.bodyToMono(RefFur.class)
                .doOnNext(validator::validate)
                .flatMap(refFur -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurService.save(refFur), RefFur.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefFur(ServerRequest request) {
        return refFurService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefFur(ServerRequest request) {
        return ok().bodyValue(refFurService.empty());
    }
}

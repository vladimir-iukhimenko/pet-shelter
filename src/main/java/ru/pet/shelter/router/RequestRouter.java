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
import ru.pet.shelter.model.Request;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RequestPetService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RequestRouter {

    private final RequestPetService requestService;
    private final EntityValidator<Request> validator;

    @Autowired
    public RequestRouter(RequestPetService requestService, EntityValidator<Request> validator) {
        this.requestService = requestService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/request", beanClass = RequestPetService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/request/{id}", beanClass = RequestPetService.class, beanMethod = "getById"),
            @RouterOperation(path = "/request/save", beanClass = RequestPetService.class, beanMethod = "save"),
            @RouterOperation(path = "/request/update/{id}", beanClass = RequestPetService.class, beanMethod = "update"),
            @RouterOperation(path = "/request/delete/{id}", beanClass = RequestPetService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/request/empty", beanClass = RequestPetService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> requestRoutes() {
        return
                route()
                        .GET("/request", this::getAllRequests)

                        .GET("/request/empty", this::emptyRequest)

                        .GET("/request/{id}", this::getRequestById)

                        .POST("/request/save", this::insertRequest)

                        .PUT("/request/update/{id}", this::updateRequest)

                        .DELETE("/request/delete/{id}", this::deleteRequest)



                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRequests(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(requestService.getAll(), Request.class);
    }

    private Mono<ServerResponse> getRequestById(ServerRequest request) {
        return requestService.getById(request.pathVariable("id"))
                .flatMap(petRequest -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(petRequest))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRequest(ServerRequest request) {
        return request.bodyToMono(Request.class)
                .doOnNext(validator::validate)
                .flatMap(petRequest -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestService.save(petRequest), Request.class));
    }

    private Mono<ServerResponse> updateRequest(ServerRequest request) {
        return request.bodyToMono(Request.class)
                .doOnNext(validator::validate)
                .flatMap(petRequest -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestService.save(petRequest), Request.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRequest(ServerRequest request) {
        return requestService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRequest(ServerRequest request) {
        return ok().body(requestService.empty(), Request.class);
    }
}

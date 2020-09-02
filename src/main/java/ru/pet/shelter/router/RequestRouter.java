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
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Request;
import ru.pet.shelter.service.RequestService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RequestRouter {

    private final RequestService requestService;
    private final Validator validator;

    @Autowired
    public RequestRouter(RequestService requestService, Validator validator) {
        this.requestService = requestService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/request", beanClass = RequestService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/request/{id}", beanClass = RequestService.class, beanMethod = "getById"),
            @RouterOperation(path = "/request/save", beanClass = RequestService.class, beanMethod = "save"),
            @RouterOperation(path = "/request/update/{id}", beanClass = RequestService.class, beanMethod = "update"),
            @RouterOperation(path = "/request/delete/{id}", beanClass = RequestService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/request/empty", beanClass = RequestService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> requestRoutes() {
        return RouterFunctions
                .route(GET("/request").and(accept(MediaType.APPLICATION_JSON)), this::getAllRequests)
                .andRoute(GET("/request/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRequestById)
                .andRoute(POST("/request/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRequest)
                .andRoute(PUT("/request/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRequest)
                .andRoute(DELETE("/request/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRequest)
                .andRoute(GET("/request/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRequest);
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
                .doOnNext(this::validate)
                .flatMap(petRequest -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestService.save(petRequest), Request.class));
    }

    private Mono<ServerResponse> updateRequest(ServerRequest request) {
        return request.bodyToMono(Request.class)
                .doOnNext(this::validate)
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
        return ok().bodyValue(requestService.empty());
    }

    private void validate(Request request) {
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

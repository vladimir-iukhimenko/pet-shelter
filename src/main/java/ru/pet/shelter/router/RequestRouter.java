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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Request;
import ru.pet.shelter.repository.ChipRepository;
import ru.pet.shelter.repository.RequestRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RequestRouter {

    private final RequestRepository requestRepository;
    private final Validator validator;

    @Autowired
    public RequestRouter(RequestRepository requestRepository, Validator validator) {
        this.requestRepository = requestRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/request", beanClass = ChipRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/request/{id}", beanClass = ChipRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> requestRoutes() {
        return
                route()
                        .GET("/request", this::getAllRequests)

                        .GET("/request/{id}", this::getRequestById)

                        .POST("/request", this::insertRequest)

                        .PUT("/request/{id}", this::updateRequest)

                        .DELETE("/request/{id}", this::deleteRequest)

                        .GET("/request/empty", this::emptyRequest)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRequests(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(requestRepository.findAll(), Request.class);
    }

    private Mono<ServerResponse> getRequestById(ServerRequest request) {
        return requestRepository.findById(request.pathVariable("id"))
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
                        .body(requestRepository.save(petRequest), Request.class));
    }

    private Mono<ServerResponse> updateRequest(ServerRequest request) {
        return request.bodyToMono(Request.class)
                .doOnNext(this::validate)
                .flatMap(petRequest -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(requestRepository.save(petRequest), Request.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRequest(ServerRequest request) {
        return requestRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRequest(ServerRequest request) {
        return ok().bodyValue(new Request());
    }

    private void validate(Request request) {
        Errors errors = new BeanPropertyBindingResult(request, "request");
        validator.validate(request, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

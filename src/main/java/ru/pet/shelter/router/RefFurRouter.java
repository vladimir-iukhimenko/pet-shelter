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
import ru.pet.shelter.model.RefFur;
import ru.pet.shelter.service.RefFurService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Configuration
public class RefFurRouter {

    private final RefFurService refFurService;
    private final Validator validator;

    @Autowired
    public RefFurRouter(RefFurService refFurService, Validator validator) {
        this.refFurService = refFurService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-Fur", beanClass = RefFurService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-Fur/{id}", beanClass = RefFurService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-Fur/save", beanClass = RefFurService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-Fur/update/{id}", beanClass = RefFurService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-Fur/delete/{id}", beanClass = RefFurService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-Fur/empty", beanClass = RefFurService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refFurRoutes() {
        return RouterFunctions
                .route(GET("/ref-Fur").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefFurs)
                .andRoute(GET("/ref-Fur/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefFurById)
                .andRoute(POST("/ref-Fur/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefFur)
                .andRoute(PUT("/ref-Fur/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefFur)
                .andRoute(DELETE("/ref-Fur/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefFur)
                .andRoute(GET("/ref-Fur/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefFur);
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
                .doOnNext(this::validate)
                .flatMap(refFur -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurService.save(refFur), RefFur.class));
    }

    private Mono<ServerResponse> updateRefFur(ServerRequest request) {
        return request.bodyToMono(RefFur.class)
                .doOnNext(this::validate)
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

    private void validate(RefFur refFur) {
        Errors errors = new BeanPropertyBindingResult(refFur, "refFur");
        validator.validate(refFur, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

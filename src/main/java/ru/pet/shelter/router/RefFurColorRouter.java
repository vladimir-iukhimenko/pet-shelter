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
import ru.pet.shelter.model.RefFurColor;
import ru.pet.shelter.service.RefFurColorService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefFurColorRouter {

    private final RefFurColorService refFurColorService;
    private final Validator validator;

    @Autowired
    public RefFurColorRouter(RefFurColorService refFurColorService, Validator validator) {
        this.refFurColorService = refFurColorService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-FurColor", beanClass = RefFurColorService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-FurColor/{id}", beanClass = RefFurColorService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-FurColor/save", beanClass = RefFurColorService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-FurColor/update/{id}", beanClass = RefFurColorService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-FurColor/delete/{id}", beanClass = RefFurColorService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-FurColor/empty", beanClass = RefFurColorService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refFurColorRoutes() {
        return RouterFunctions
                .route(GET("/ref-FurColor").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefFurColors)
                .andRoute(GET("/ref-FurColor/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefFurColorById)
                .andRoute(POST("/ref-FurColor/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefFurColor)
                .andRoute(PUT("/ref-FurColor/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefFurColor)
                .andRoute(DELETE("/ref-FurColor/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefFurColor)
                .andRoute(GET("/ref-FurColor/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefFurColor);

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
                .doOnNext(this::validate)
                .flatMap(refFurColor -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurColorService.save(refFurColor), RefFurColor.class));
    }

    private Mono<ServerResponse> updateRefFurColor(ServerRequest request) {
        return request.bodyToMono(RefFurColor.class)
                .doOnNext(this::validate)
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

    private void validate(RefFurColor refFurColor) {
        Errors errors = new BeanPropertyBindingResult(refFurColor, "refFurColor");
        validator.validate(refFurColor, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

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
import ru.pet.shelter.model.RefFurColor;
import ru.pet.shelter.repository.RefFurColorRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefFurColorRouter {

    private final RefFurColorRepository refFurColorRepository;
    private final Validator validator;

    @Autowired
    public RefFurColorRouter(RefFurColorRepository refFurColorRepository, Validator validator) {
        this.refFurColorRepository = refFurColorRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-furColor", beanClass = RefFurColorRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-furColor/{id}", beanClass = RefFurColorRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/ref-furColor", this::getAllRefFurColors)

                        .GET("/ref-furColor/{id}", this::getRefFurColorById)

                        .POST("/ref-furColor", this::insertRefFurColor)

                        .PUT("/ref-furColor/{id}", this::updateRefFurColor)

                        .DELETE("/ref-furColor/{id}", this::deleteRefFurColor)

                        .GET("/ref-furColor/empty", this::emptyRefFurColor)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefFurColors(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refFurColorRepository.findAll(), RefFurColor.class);
    }

    private Mono<ServerResponse> getRefFurColorById(ServerRequest request) {
        return refFurColorRepository.findById(request.pathVariable("id"))
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
                        .body(refFurColorRepository.save(refFurColor), RefFurColor.class));
    }

    private Mono<ServerResponse> updateRefFurColor(ServerRequest request) {
        return request.bodyToMono(RefFurColor.class)
                .doOnNext(this::validate)
                .flatMap(refFurColor -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurColorRepository.save(refFurColor), RefFurColor.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefFurColor(ServerRequest request) {
        return refFurColorRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefFurColor(ServerRequest request) {
        return ok().bodyValue(new RefFurColor());
    }

    private void validate(RefFurColor refFurColor) {
        Errors errors = new BeanPropertyBindingResult(refFurColor, "refFurColor");
        validator.validate(refFurColor, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

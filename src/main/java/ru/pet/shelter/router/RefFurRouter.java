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
import ru.pet.shelter.model.RefFur;
import ru.pet.shelter.repository.RefFurRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;


@Configuration
public class RefFurRouter {

    private final RefFurRepository refFurRepository;
    private final Validator validator;

    @Autowired
    public RefFurRouter(RefFurRepository refFurRepository, Validator validator) {
        this.refFurRepository = refFurRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/ref-fur", beanClass = RefFurRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/ref-fur/{id}", beanClass = RefFurRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> refFurRoutes() {
        return
                route()
                        .GET("/ref-fur", this::getAllRefFurs)

                        .GET("/ref-fur/{id}", this::getRefFurById)

                        .POST("/ref-fur", this::insertRefFur)

                        .PUT("/ref-fur/{id}", this::updateRefFur)

                        .DELETE("/ref-fur/{id}", this::deleteRefFur)

                        .GET("/ref-fur/empty", this::emptyRefFur)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefFurs(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refFurRepository.findAll(), RefFur.class);
    }

    private Mono<ServerResponse> getRefFurById(ServerRequest request) {
        return refFurRepository.findById(request.pathVariable("id"))
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
                        .body(refFurRepository.save(refFur), RefFur.class));
    }

    private Mono<ServerResponse> updateRefFur(ServerRequest request) {
        return request.bodyToMono(RefFur.class)
                .doOnNext(this::validate)
                .flatMap(refFur -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refFurRepository.save(refFur), RefFur.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefFur(ServerRequest request) {
        return refFurRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefFur(ServerRequest request) {
        return ok().bodyValue(new RefFur());
    }

    private void validate(RefFur refFur) {
        Errors errors = new BeanPropertyBindingResult(refFur, "refFur");
        validator.validate(refFur, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

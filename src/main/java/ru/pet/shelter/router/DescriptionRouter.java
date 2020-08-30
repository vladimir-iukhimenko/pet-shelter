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
import ru.pet.shelter.model.Description;
import ru.pet.shelter.repository.DescriptionRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class DescriptionRouter {

    private final DescriptionRepository descriptionRepository;
    private final Validator validator;

    @Autowired
    public DescriptionRouter(DescriptionRepository descriptionRepository, Validator validator) {
        this.descriptionRepository = descriptionRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/description", beanClass = DescriptionRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/description/{id}", beanClass = DescriptionRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> descriptionRoutes() {
        return
                route()
                        .GET("/description", this::getAllDescriptions)

                        .GET("/description/{id}", this::getDescriptionById)

                        .POST("/description", this::insertDescription)

                        .PUT("/description/{id}", this::updateDescription)

                        .DELETE("/description/{id}", this::deleteDescription)

                        .GET("/description/empty", this::emptyDescription)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllDescriptions(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(descriptionRepository.findAll(), Description.class);
    }

    private Mono<ServerResponse> getDescriptionById(ServerRequest request) {
        return descriptionRepository.findById(request.pathVariable("id"))
                .flatMap(description -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(description))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertDescription(ServerRequest request) {
        return request.bodyToMono(Description.class)
                .doOnNext(this::validate)
                .flatMap(description -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(descriptionRepository.save(description), Description.class));
    }

    private Mono<ServerResponse> updateDescription(ServerRequest request) {
        return request.bodyToMono(Description.class)
                .doOnNext(this::validate)
                .flatMap(description -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(descriptionRepository.save(description), Description.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteDescription(ServerRequest request) {
        return descriptionRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyDescription(ServerRequest request) {
        return ok().bodyValue(new Description());
    }

    private void validate(Description description) {
        Errors errors = new BeanPropertyBindingResult(description, "description");
        validator.validate(description, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

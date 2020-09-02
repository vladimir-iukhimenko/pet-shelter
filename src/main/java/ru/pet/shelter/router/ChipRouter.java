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
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.service.ChipService;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.http.HttpStatus.*;


@Configuration
public class ChipRouter {

    private final Validator validator;
    private final ChipService chipService;

    @Autowired
    public ChipRouter(ChipService chipService, Validator validator) {
        this.validator = validator;
        this.chipService = chipService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/chip", beanClass = ChipService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/chip/{id}", beanClass = ChipService.class, beanMethod = "getById"),
            @RouterOperation(path = "/chip/save", beanClass = ChipService.class, beanMethod = "save"),
            @RouterOperation(path = "/chip/update/{id}", beanClass = ChipService.class, beanMethod = "update"),
            @RouterOperation(path = "/chip/delete/{id}", beanClass = ChipService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/chip/empty", beanClass = ChipService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> chipRoutes() {
        return RouterFunctions
                .route(GET("/chip").and(accept(MediaType.APPLICATION_JSON)), this::getAllChips)
                .andRoute(GET("/chip/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getChipById)
                .andRoute(POST("/chip/save").and(accept(MediaType.APPLICATION_JSON)), this::insertChip)
                .andRoute(PUT("/chip/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateChip)
                .andRoute(DELETE("/chip/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteChip)
                .andRoute(GET("/chip/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyChip);

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllChips(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(chipService.getAll(), Chip.class);
    }

    private Mono<ServerResponse> getChipById(ServerRequest request) {
        return chipService.getById(request.pathVariable("id"))
                .flatMap(chip -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(chip))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .doOnNext(this::validate)
                .flatMap(chip -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(chipService.save(chip), Chip.class));
    }

    private Mono<ServerResponse> updateChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .doOnNext(this::validate)
                .flatMap(chip -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(chipService.save(chip), Chip.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteChip(ServerRequest request) {
        return chipService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyChip(ServerRequest request) {
        return ok().bodyValue(chipService.empty());
    }

    private void validate(Chip chip) {
        Errors errors = new BeanPropertyBindingResult(chip, "chip");
        validator.validate(chip, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

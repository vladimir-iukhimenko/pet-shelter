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
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.repository.ChipRepository;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.http.HttpStatus.*;


@Configuration
public class ChipRouter {

    @Autowired
    private ChipRepository chipRepository;

    @Autowired
    private Validator validator;

    @Bean
    @RouterOperations({@RouterOperation(path = "/chip", beanClass = ChipRepository.class, beanMethod = "findAll"),
                        @RouterOperation(path = "/chip/{id}", beanClass = ChipRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/chip", this::getAllChips)

                        .GET("/chip/{id}", this::getChipById)

                        .POST("/chip", this::insertChip)

                        .PUT("/chip/{id}", this::updateChip)

                        .DELETE("/chip/{id}", this::deleteChip)

                        .GET("/chip/empty", this::emptyChip)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllChips(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(chipRepository.findAll(), Chip.class);
    }

    private Mono<ServerResponse> getChipById(ServerRequest request) {
        return chipRepository.findById(request.pathVariable("id"))
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
                        .body(chipRepository.save(chip), Chip.class));
    }

    private Mono<ServerResponse> updateChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .doOnNext(this::validate)
                .flatMap(chip -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(chipRepository.save(chip), Chip.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteChip(ServerRequest request) {
        return chipRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyChip(ServerRequest request) {
        return ok().bodyValue(new Chip());
    }

    private void validate(Chip chip) {
        Errors errors = new BeanPropertyBindingResult(chip, "chip");
        validator.validate(chip, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

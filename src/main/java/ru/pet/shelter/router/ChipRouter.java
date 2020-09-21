package ru.pet.shelter.router;

import org.bson.types.ObjectId;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.ChipService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;


@Configuration
public class ChipRouter {

    private final ChipService chipService;
    private final EntityValidator<Chip> validator;


    @Autowired
    public ChipRouter(ChipService chipService, EntityValidator<Chip> validator) {
        this.chipService = chipService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/chip", method = RequestMethod.GET, beanClass = ChipService.class, beanMethod = "findAll"),
            @RouterOperation(path = "/chip/empty", method = RequestMethod.GET, beanClass = ChipService.class, beanMethod = "empty"),
            @RouterOperation(path = "/chip/{id}", method = RequestMethod.GET, beanClass = ChipService.class, beanMethod = "findById"),
            @RouterOperation(path = "/chip/{id}", method = RequestMethod.POST, beanClass = ChipService.class, beanMethod = "save"),
            @RouterOperation(path = "/chip/{id}", method = RequestMethod.PUT, beanClass = ChipService.class, beanMethod = "update"),
            @RouterOperation(path = "/chip/{id}", method = RequestMethod.DELETE, beanClass = ChipService.class, beanMethod = "removeById")
    })
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/chip", this::getAllChips)

                        .GET("/chip/empty", this::emptyChip)

                        .GET("/chip/{id}", this::getChipById)

                        .POST("/chip/{id}", this::insertChip)

                        .PUT("/chip/{id}", this::updateChip)

                        .DELETE("/chip/{id}", this::deleteChip)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllChips(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(chipService.findAll(), Chip.class);
    }

    private Mono<ServerResponse> getChipById(ServerRequest request) {
        return chipService.findById(request.pathVariable("id"))
                .flatMap(chip -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(chip))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .doOnNext(validator::validate)
                .doOnNext(chip -> chip.setId(new ObjectId().toHexString()))
                .flatMap(chip -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(chipService.save(request.pathVariable("id"), chip), Chip.class));
    }

    private Mono<ServerResponse> updateChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .doOnNext(validator::validate)
                .flatMap(chip -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(chipService.update(request.pathVariable("id"), chip), Chip.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> emptyChip(ServerRequest request) {
        return ok().body(chipService.empty(), Chip.class);
    }

    private Mono<ServerResponse> deleteChip(ServerRequest request) {
        return chipService.removeById(request.pathVariable("id"))
                .then(noContent().build());
    }

}

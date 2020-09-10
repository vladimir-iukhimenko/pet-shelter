package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.service.ChipService;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;


@Configuration
public class ChipRouter {

    private final ChipService chipService;

    @Autowired
    public ChipRouter(ChipService chipService) {
        this.chipService = chipService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/chip", beanClass = ChipService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/chip/{id}", beanClass = ChipService.class, beanMethod = "getById"),
            @RouterOperation(path = "/chip/empty", beanClass = ChipService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> chipRoutes() {
        return
                route()
                        .GET("/chip", this::getAllChips)
                        .GET("/chip/empty", this::emptyChip)
                        .GET("/chip/{id}", this::getChipById)
                        .build();
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

    private Mono<ServerResponse> emptyChip(ServerRequest request) {
        return ok().body(chipService.empty(), Chip.class);
    }

}

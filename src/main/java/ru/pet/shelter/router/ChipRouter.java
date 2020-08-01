package ru.pet.shelter.router;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.repository.ChipRepository;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class ChipRouter {

    @Autowired
    private ChipRepository chipRepository;

    @RouterOperations({@RouterOperation(path = "/chip", beanClass = ChipRepository.class, beanMethod = "findAll"),
                        @RouterOperation(path = "/chip/{id}", beanClass = ChipRepository.class, beanMethod = "findById")})
    @Bean
    RouterFunction<?> chipRoutes() {
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


    private Mono<ServerResponse> getAllChips(ServerRequest request) {
        return ok().body(chipRepository.findAll(), Chip.class);
    }

    private Mono<ServerResponse> getChipById(ServerRequest request) {
        return chipRepository.findById(request.pathVariable("id"))
                .flatMap(chip -> ok().bodyValue(chip))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> insertChip(ServerRequest request) {
        return request.bodyToMono(Chip.class)
                .flatMap(chip -> ok().body(chipRepository.save(chip), Chip.class));
    }

    private Mono<ServerResponse> updateChip(ServerRequest request) {
        return chipRepository.findById(request.pathVariable("id"))
                .flatMap(chip -> ok().body(chipRepository.save(chip), Chip.class))
                .switchIfEmpty(notFound().build());
    }

    private Mono<ServerResponse> deleteChip(ServerRequest request) {
        return chipRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyChip(ServerRequest request) {
        return ok().bodyValue(new Chip());
    }
}

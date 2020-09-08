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
import ru.pet.shelter.model.RefVaccination;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.RefVaccinationService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefVaccinationRouter {

    private final RefVaccinationService refVaccinationService;
    private final EntityValidator<RefVaccination> validator;

    @Autowired
    public RefVaccinationRouter(RefVaccinationService refVaccinationService, EntityValidator<RefVaccination> validator) {
        this.refVaccinationService = refVaccinationService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/vaccination", beanClass = RefVaccinationService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/vaccination/{id}", beanClass = RefVaccinationService.class, beanMethod = "getById"),
            @RouterOperation(path = "/vaccination/save", beanClass = RefVaccinationService.class, beanMethod = "save"),
            @RouterOperation(path = "/vaccination/update/{id}", beanClass = RefVaccinationService.class, beanMethod = "update"),
            @RouterOperation(path = "/vaccination/delete/{id}", beanClass = RefVaccinationService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/vaccination/empty", beanClass = RefVaccinationService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refVaccinationRoutes() {
        return
                route()
                        .GET("/vaccination", this::getAllRefVaccinations)

                        .GET("/vaccination/{id}", this::getRefVaccinationById)

                        .POST("/vaccination/save", this::insertRefVaccination)

                        .PUT("/vaccination/update/{id}", this::updateRefVaccination)

                        .DELETE("/vaccination/delete/{id}", this::deleteRefVaccination)

                        .GET("/vaccination/empty", this::emptyRefVaccination)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllRefVaccinations(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(refVaccinationService.getAll(), RefVaccination.class);
    }

    private Mono<ServerResponse> getRefVaccinationById(ServerRequest request) {
        return refVaccinationService.getById(request.pathVariable("id"))
                .flatMap(refVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(refVaccination))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertRefVaccination(ServerRequest request) {
        return request.bodyToMono(RefVaccination.class)
                .doOnNext(validator::validate)
                .flatMap(refVaccination -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refVaccinationService.save(refVaccination), RefVaccination.class));
    }

    private Mono<ServerResponse> updateRefVaccination(ServerRequest request) {
        return request.bodyToMono(RefVaccination.class)
                .doOnNext(validator::validate)
                .flatMap(refVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refVaccinationService.save(refVaccination), RefVaccination.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deleteRefVaccination(ServerRequest request) {
        return refVaccinationService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyRefVaccination(ServerRequest request) {
        return ok().bodyValue(refVaccinationService);
    }
}

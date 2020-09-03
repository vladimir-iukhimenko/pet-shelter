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
import ru.pet.shelter.model.PassportVaccination;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.PassportVaccinationService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportVaccinationRouter {

    private final PassportVaccinationService passportVaccinationService;
    private final EntityValidator<PassportVaccination> validator;

    @Autowired
    public PassportVaccinationRouter(PassportVaccinationService passportVaccinationService, EntityValidator<PassportVaccination> validator) {
        this.passportVaccinationService = passportVaccinationService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/pass-vaccine", beanClass = PassportVaccinationService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/pass-vaccine/{id}", beanClass = PassportVaccinationService.class, beanMethod = "getById"),
            @RouterOperation(path = "/pass-vaccine/save", beanClass = PassportVaccinationService.class, beanMethod = "save"),
            @RouterOperation(path = "/pass-vaccine/update/{id}", beanClass = PassportVaccinationService.class, beanMethod = "update"),
            @RouterOperation(path = "/pass-vaccine/delete/{id}", beanClass = PassportVaccinationService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/pass-vaccine/empty", beanClass = PassportVaccinationService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> passportVaccinationRoutes() {
        return
                route()
                        .GET("/pass-vaccine", this::getAllPassportVaccinations)

                        .GET("/pass-vaccine/{id}", this::getPassportVaccinationById)

                        .POST("/pass-vaccine/save", this::insertPassportVaccination)

                        .PUT("/pass-vaccine/update/{id}", this::updatePassportVaccination)

                        .DELETE("/pass-vaccine/delete/{id}", this::deletePassportVaccination)

                        .GET("/pass-vaccine/empty", this::emptyPassportVaccination)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPassportVaccinations(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(passportVaccinationService.getAll(), PassportVaccination.class);
    }

    private Mono<ServerResponse> getPassportVaccinationById(ServerRequest request) {
        return passportVaccinationService.getById(request.pathVariable("id"))
                .flatMap(passportVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(passportVaccination))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPassportVaccination(ServerRequest request) {
        return request.bodyToMono(PassportVaccination.class)
                .doOnNext(validator::validate)
                .flatMap(passportVaccination -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportVaccinationService.save(passportVaccination), PassportVaccination.class));
    }

    private Mono<ServerResponse> updatePassportVaccination(ServerRequest request) {
        return request.bodyToMono(PassportVaccination.class)
                .doOnNext(validator::validate)
                .flatMap(passportVaccination -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportVaccinationService.save(passportVaccination), PassportVaccination.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePassportVaccination(ServerRequest request) {
        return passportVaccinationService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPassportVaccination(ServerRequest request) {
        return ok().bodyValue(passportVaccinationService.empty());
    }
}

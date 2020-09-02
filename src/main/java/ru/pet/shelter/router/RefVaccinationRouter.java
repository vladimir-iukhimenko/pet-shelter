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
import ru.pet.shelter.model.RefVaccination;
import ru.pet.shelter.service.RefVaccinationService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class RefVaccinationRouter {

    private final RefVaccinationService refVaccinationService;
    private final Validator validator;

    @Autowired
    public RefVaccinationRouter(RefVaccinationService refVaccinationService, Validator validator) {
        this.refVaccinationService = refVaccinationService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/ref-Vaccination", beanClass = RefVaccinationService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/ref-Vaccination/{id}", beanClass = RefVaccinationService.class, beanMethod = "getById"),
            @RouterOperation(path = "/ref-Vaccination/save", beanClass = RefVaccinationService.class, beanMethod = "save"),
            @RouterOperation(path = "/ref-Vaccination/update/{id}", beanClass = RefVaccinationService.class, beanMethod = "update"),
            @RouterOperation(path = "/ref-Vaccination/delete/{id}", beanClass = RefVaccinationService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/ref-Vaccination/empty", beanClass = RefVaccinationService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> refVaccinationRoutes() {
        return RouterFunctions
                .route(GET("/ref-Vaccination").and(accept(MediaType.APPLICATION_JSON)), this::getAllRefVaccinations)
                .andRoute(GET("/ref-Vaccination/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getRefVaccinationById)
                .andRoute(POST("/ref-Vaccination/save").and(accept(MediaType.APPLICATION_JSON)), this::insertRefVaccination)
                .andRoute(PUT("/ref-Vaccination/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updateRefVaccination)
                .andRoute(DELETE("/ref-Vaccination/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deleteRefVaccination)
                .andRoute(GET("/ref-Vaccination/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyRefVaccination);
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
                .doOnNext(this::validate)
                .flatMap(refVaccination -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(refVaccinationService.save(refVaccination), RefVaccination.class));
    }

    private Mono<ServerResponse> updateRefVaccination(ServerRequest request) {
        return request.bodyToMono(RefVaccination.class)
                .doOnNext(this::validate)
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

    private void validate(RefVaccination refVaccination) {
        Errors errors = new BeanPropertyBindingResult(refVaccination, "refVaccination");
        validator.validate(refVaccination, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

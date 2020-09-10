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
import ru.pet.shelter.service.PassportVaccinationService;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportVaccinationRouter {

    private final PassportVaccinationService passportVaccinationService;

    @Autowired
    public PassportVaccinationRouter(PassportVaccinationService passportVaccinationService) {
        this.passportVaccinationService = passportVaccinationService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/pass-vaccine", beanClass = PassportVaccinationService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/pass-vaccine/{id}", beanClass = PassportVaccinationService.class, beanMethod = "getById"),
            @RouterOperation(path = "/pass-vaccine/delete/{id}", beanClass = PassportVaccinationService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/pass-vaccine/empty", beanClass = PassportVaccinationService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> passportVaccinationRoutes() {
        return
                route()
                        .GET("/pass-vaccine", this::getAllPassportVaccinations)
                        .GET("/pass-vaccine/empty", this::emptyPassportVaccination)
                        .GET("/pass-vaccine/{id}", this::getPassportVaccinationById)
                        .DELETE("/pass-vaccine/delete/{id}", this::deletePassportVaccination)
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

    private Mono<ServerResponse> emptyPassportVaccination(ServerRequest request) {
        return ok().body(passportVaccinationService.empty(), PassportVaccination.class);
    }

    private Mono<ServerResponse> deletePassportVaccination(ServerRequest request) {
        return passportVaccinationService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }
}

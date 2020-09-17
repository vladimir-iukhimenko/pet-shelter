package ru.pet.shelter.router;

import org.bson.types.ObjectId;
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
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.PassportService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class PassportRouter {

    private final PassportService passportService;
    private final EntityValidator<Passport> validator;

    @Autowired
    public PassportRouter(PassportService passportService, EntityValidator<Passport> validator) {
        this.passportService = passportService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/passport", beanClass = PassportService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/passport/{id}", beanClass = PassportService.class, beanMethod = "getById"),
            @RouterOperation(path = "/passport/empty", beanClass = PassportService.class, beanMethod = "empty"),
            @RouterOperation(path = "/passport/update/{id}", beanClass = PassportService.class, beanMethod = "update"),
            @RouterOperation(path = "/passport/{id}", beanClass = PassportService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/passport/empty", beanClass = PassportService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> passportRoutes() {
        return
                route()
                        .GET("/passport", this::getAllPassports)

                        .GET("/passport/empty", this::emptyPassport)

                        .GET("/passport/{id}", this::getPassportById)

                        .POST("/chip/save", this::insertPassport)

                        .PUT("/chip/update", this::updatePassport)

                        .DELETE("/chip/{id}", this::deletePassport)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPassports(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(passportService.getAll(), Passport.class);
    }

    private Mono<ServerResponse> getPassportById(ServerRequest request) {
        return passportService.getById(request.pathVariable("id"))
                .flatMap(passport -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(passport))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPassport(ServerRequest request) {
        return request.bodyToMono(Passport.class)
                .doOnNext(validator::validate)
                .doOnNext(passport -> passport.setId(new ObjectId().toHexString()))
                .flatMap(passport -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportService.save(passport), Passport.class));
    }

    private Mono<ServerResponse> updatePassport(ServerRequest request) {
        return request.bodyToMono(Passport.class)
                .doOnNext(validator::validate)
                .flatMap(passport -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(passportService.update(passport), Passport.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> emptyPassport(ServerRequest request) {
        return ok().body(passportService.empty(), Passport.class);
    }

    private Mono<ServerResponse> deletePassport(ServerRequest request) {
        return passportService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }
}

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
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.service.PassportService;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PassportRouter {

    private final PassportService passportService;

    @Autowired
    public PassportRouter(PassportService passportService) {
        this.passportService = passportService;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/passport", beanClass = PassportService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/passport/{id}", beanClass = PassportService.class, beanMethod = "getById"),
            @RouterOperation(path = "/passport/empty", beanClass = PassportService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> passportRoutes() {
        return
                route()
                        .GET("/passport", this::getAllPassports)
                        .GET("/passport/empty", this::emptyPassport)
                        .GET("/passport/{id}", this::getPassportById)
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

    private Mono<ServerResponse> emptyPassport(ServerRequest request) {
        return ok().body(passportService.empty(), Passport.class);
    }
}

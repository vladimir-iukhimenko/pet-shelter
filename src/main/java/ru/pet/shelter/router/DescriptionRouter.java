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
import ru.pet.shelter.model.Description;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.DescriptionService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Configuration
public class DescriptionRouter {

    private final DescriptionService descriptionService;
    private final EntityValidator<Description> validator;


    @Autowired
    public DescriptionRouter(DescriptionService descriptionService, EntityValidator<Description> validator) {
        this.descriptionService = descriptionService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/description/all", beanClass = DescriptionService.class, beanMethod = "findAll"),
            @RouterOperation(path = "/description/byPet/{id}", beanClass = DescriptionService.class, beanMethod = "findAllByPetId"),
            @RouterOperation(path = "/description/empty", beanClass = DescriptionService.class, beanMethod = "empty"),
            @RouterOperation(path = "/description/{id}", beanClass = DescriptionService.class, beanMethod = "findById"),
            @RouterOperation(path = "/description/save/{id}", beanClass = DescriptionService.class, beanMethod = "save"),
            @RouterOperation(path = "/description/update/{id}", beanClass = DescriptionService.class, beanMethod = "update"),
            @RouterOperation(path = "/description/{id}", beanClass = DescriptionService.class, beanMethod = "removeById")

    })
    RouterFunction<ServerResponse> descriptionRoutes() {
        return
                route()
                        .GET("/description/all", this::getAllDescriptions)

                        .GET("/description/byPet/{id}", this::getAllDescriptionsFromPet)

                        .GET("/description/empty", this::emptyDescription)

                        .GET("/description/{id}", this::getDescriptionById)

                        .POST("/description/save/{id}", this::insertDescription)

                        .PUT("/description/update/{id}", this::updateDescription)

                        .DELETE("/description/{id}", this::deleteDescription)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllDescriptions(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(descriptionService.findAll(), Description.class);
    }

    private Mono<ServerResponse> getAllDescriptionsFromPet(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(descriptionService.findAllByPetId(request.pathVariable("id")), Description.class);
    }

    private Mono<ServerResponse> getDescriptionById(ServerRequest request) {
        return descriptionService.findById(request.pathVariable("id"))
                .flatMap(description -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(description))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertDescription(ServerRequest request) {
        return request.bodyToMono(Description.class)
                .doOnNext(validator::validate)
                .doOnNext(description -> description.setId(new ObjectId().toHexString()))
                .flatMap(description -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(descriptionService.save(request.pathVariable("id"), description), Description.class));
    }

    private Mono<ServerResponse> updateDescription(ServerRequest request) {
        return request.bodyToMono(Description.class)
                .doOnNext(validator::validate)
                .flatMap(description -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(descriptionService.update(request.pathVariable("id"), description), Description.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> emptyDescription(ServerRequest request) {
        return ok().body(descriptionService.empty(), Description.class);
    }

    private Mono<ServerResponse> deleteDescription(ServerRequest request) {
        return descriptionService.removeById(request.pathVariable("id"))
                .then(noContent().build());
    }
}

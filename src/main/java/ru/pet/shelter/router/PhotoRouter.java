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
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.PhotoService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PhotoRouter {

    private final PhotoService photoService;
    private final EntityValidator<Photo> validator;

    @Autowired
    public PhotoRouter(PhotoService photoService, EntityValidator<Photo> validator) {
        this.photoService = photoService;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(path = "/photo", beanClass = PhotoService.class, beanMethod = "getAll"),
            @RouterOperation(path = "/photo/{id}", beanClass = PhotoService.class, beanMethod = "getById"),
            @RouterOperation(path = "/photo/save", beanClass = PhotoService.class, beanMethod = "save"),
            @RouterOperation(path = "/photo/update/{id}", beanClass = PhotoService.class, beanMethod = "update"),
            @RouterOperation(path = "/photo/delete/{id}", beanClass = PhotoService.class, beanMethod = "deleteById"),
            @RouterOperation(path = "/photo/empty", beanClass = PhotoService.class, beanMethod = "empty")
    })
    RouterFunction<ServerResponse> photoRoutes() {
        return
                route()
                        .GET("/photo", this::getAllPhotos)

                        .GET("/photo/{id}", this::getPhotoById)

                        .POST("/photo/save", this::insertPhoto)

                        .PUT("/photo/update/{id}", this::updatePhoto)

                        .DELETE("/photo/delete/{id}", this::deletePhoto)

                        .GET("/photo/empty", this::emptyPhoto)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPhotos(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(photoService.getAll(), Photo.class);
    }

    private Mono<ServerResponse> getPhotoById(ServerRequest request) {
        return photoService.getById(request.pathVariable("id"))
                .flatMap(photo -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(photo))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(validator::validate)
                .flatMap(photo -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoService.save(photo), Photo.class));
    }

    private Mono<ServerResponse> updatePhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(validator::validate)
                .flatMap(photo -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoService.save(photo), Photo.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePhoto(ServerRequest request) {
        return photoService.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPhoto(ServerRequest request) {
        return ok().bodyValue(photoService.empty());
    }

}

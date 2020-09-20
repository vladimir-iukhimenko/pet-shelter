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
            @RouterOperation(path = "/photo/all", beanClass = PhotoService.class, beanMethod = "findAll"),
            @RouterOperation(path = "/photo/byPet/{id}", beanClass = PhotoService.class, beanMethod = "findAllByPetId"),
            @RouterOperation(path = "/photo/empty", beanClass = PhotoService.class, beanMethod = "empty"),
            @RouterOperation(path = "/photo/{id}", beanClass = PhotoService.class, beanMethod = "findById"),
            @RouterOperation(path = "/photo/save/{id}", beanClass = PhotoService.class, beanMethod = "save"),
            @RouterOperation(path = "/photo/update/{id}", beanClass = PhotoService.class, beanMethod = "update"),
            @RouterOperation(path = "/photo/delete/{id}", beanClass = PhotoService.class, beanMethod = "deleteById")
    })
    RouterFunction<ServerResponse> photoRoutes() {
        return
                route()
                        .GET("/photo/all", this::getAllPhotos)

                        .GET("/photo/byPet/{id}", this::getAllPhotosFromPet)

                        .GET("/photo/empty", this::emptyPhoto)

                        .GET("/photo/{id}", this::getPhotoById)

                        .POST("/photo/save/{id}", this::insertPhoto)

                        .PUT("/photo/update/{id}", this::updatePhoto)

                        .DELETE("/photo/delete/{id}", this::deletePhoto)

                        .build();
    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPhotos(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(photoService.findAll(), Photo.class);
    }

    private Mono<ServerResponse> getAllPhotosFromPet(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON)
                .body(photoService.findAllByPet(request.pathVariable("id")), Photo.class);
    }

    private Mono<ServerResponse> getPhotoById(ServerRequest request) {
        return photoService.findById(request.pathVariable("id"))
                .flatMap(photo -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(photo))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> insertPhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(validator::validate)
                .doOnNext(photo -> photo.setId(new ObjectId().toHexString()))
                .flatMap(photo -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoService.save(request.pathVariable("id"), photo), Photo.class));
    }

    private Mono<ServerResponse> updatePhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(validator::validate)
                .flatMap(photo -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoService.update(request.pathVariable("id"), photo), Photo.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePhoto(ServerRequest request) {
        return photoService.removeById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPhoto(ServerRequest request) {
        return ok().body(photoService.empty(), Photo.class);
    }

}

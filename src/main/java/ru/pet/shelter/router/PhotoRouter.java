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
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.service.PhotoService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PhotoRouter {

    private final PhotoService photoService;
    private final Validator validator;

    @Autowired
    public PhotoRouter(PhotoService photoService, Validator validator) {
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
        return RouterFunctions
                .route(GET("/photo").and(accept(MediaType.APPLICATION_JSON)), this::getAllPhotos)
                .andRoute(GET("/photo/{id}").and(accept(MediaType.APPLICATION_JSON)), this::getPhotoById)
                .andRoute(POST("/photo/save").and(accept(MediaType.APPLICATION_JSON)), this::insertPhoto)
                .andRoute(PUT("/photo/update/{id}").and(accept(MediaType.APPLICATION_JSON)), this::updatePhoto)
                .andRoute(DELETE("/photo/delete/{id}").and(accept(MediaType.APPLICATION_JSON)), this::deletePhoto)
                .andRoute(GET("/photo/empty").and(accept(MediaType.APPLICATION_JSON)), this::emptyPhoto);

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
                .doOnNext(this::validate)
                .flatMap(photo -> status(CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoService.save(photo), Photo.class));
    }

    private Mono<ServerResponse> updatePhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(this::validate)
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

    private void validate(Photo photo) {
        Errors errors = new BeanPropertyBindingResult(photo, "photo");
        validator.validate(photo, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

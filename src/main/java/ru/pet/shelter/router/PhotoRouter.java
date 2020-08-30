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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.repository.PhotoRepository;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class PhotoRouter {

    private final PhotoRepository photoRepository;
    private final Validator validator;

    @Autowired
    public PhotoRouter(PhotoRepository photoRepository, Validator validator) {
        this.photoRepository = photoRepository;
        this.validator = validator;
    }

    @Bean
    @RouterOperations({@RouterOperation(path = "/photo", beanClass = PhotoRepository.class, beanMethod = "findAll"),
            @RouterOperation(path = "/photo/{id}", beanClass = PhotoRepository.class, beanMethod = "findById")})
    RouterFunction<ServerResponse> photoRoutes() {
        return
                route()
                        .GET("/photo", this::getAllPhotos)

                        .GET("/photo/{id}", this::getPhotoById)

                        .POST("/photo", this::insertPhoto)

                        .PUT("/photo/{id}", this::updatePhoto)

                        .DELETE("/photo/{id}", this::deletePhoto)

                        .GET("/photo/empty", this::emptyPhoto)

                        .build();

    }

    Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    private Mono<ServerResponse> getAllPhotos(ServerRequest request) {
        return ok().contentType(MediaType.APPLICATION_JSON).body(photoRepository.findAll(), Photo.class);
    }

    private Mono<ServerResponse> getPhotoById(ServerRequest request) {
        return photoRepository.findById(request.pathVariable("id"))
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
                        .body(photoRepository.save(photo), Photo.class));
    }

    private Mono<ServerResponse> updatePhoto(ServerRequest request) {
        return request.bodyToMono(Photo.class)
                .doOnNext(this::validate)
                .flatMap(photo -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(photoRepository.save(photo), Photo.class))
                .switchIfEmpty(notFound);
    }

    private Mono<ServerResponse> deletePhoto(ServerRequest request) {
        return photoRepository.deleteById(request.pathVariable("id"))
                .then(noContent().build());
    }

    private Mono<ServerResponse> emptyPhoto(ServerRequest request) {
        return ok().bodyValue(new Photo());
    }

    private void validate(Photo photo) {
        Errors errors = new BeanPropertyBindingResult(photo, "photo");
        validator.validate(photo, errors);
        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }
}

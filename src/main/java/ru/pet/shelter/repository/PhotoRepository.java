package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.model.Photo;

@Repository
public interface PhotoRepository {
    Flux<Photo> findAllPhoto();
    Flux<Photo> findAllPhotoByPet(String id);
    Mono<Photo> findPhotoById(String id);
    Mono<? extends Pet> savePhoto(String id, Photo photo);
    Mono<? extends Pet> savePhotoAsAvatar(String id, Photo photo);
    Mono<? extends Pet> updatePhoto(String id, Photo photo);
    Mono<? extends Pet> updatePhotoAsAvatar(String id, Photo photo);
    Mono<? extends Pet> removePhotoById(String id);
    Mono<Photo> emptyPhoto();
}

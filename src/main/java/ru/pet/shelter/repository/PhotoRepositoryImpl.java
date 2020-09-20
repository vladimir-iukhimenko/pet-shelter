package ru.pet.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.model.Photo;

public class PhotoRepositoryImpl implements PhotoRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public PhotoRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Photo> findAllPhoto() {
        Query query = new Query(Criteria.where("avatar").exists(true).orOperator(Criteria.where("photos").exists(true)));
        return mongoTemplate.find(query, Pet.class)
                .flatMap(pet -> Flux.fromIterable(pet.getPhotos()).concatWithValues(pet.getAvatar()));
    }

    @Override
    public Flux<Photo> findAllPhotoByPet(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Pet.class).flatMapIterable(Pet::getPhotos);
    }

    @Override
    public Mono<Photo> findPhotoById(String id) {
        return findAllPhoto().filter(photo -> photo.getId().equals(id)).next();
    }

    @Override
    public Mono<? extends Pet> savePhoto(String id, Photo photo) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("photos", photo);
        return mongoTemplate.findAndModify(query, update, Pet.class);
    }

    @Override
    public Mono<? extends Pet> savePhotoAsAvatar(String id, Photo photo) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("avatar", photo);
        return mongoTemplate.findAndModify(query, update, Pet.class);
    }

    @Override
    public Mono<? extends Pet> updatePhoto(String id, Photo photo) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Pet.class)
                .zipWith(findAllPhotoByPet(id)
                                .concatWithValues(photo)
                                .collectList(),
                        (pet, photos) -> {
                            pet.setPhotos(photos);
                            return pet;
                        })
                .flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<? extends Pet> updatePhotoAsAvatar(String id, Photo photo) {
        return savePhotoAsAvatar(id, photo);
    }

    @Override
    public Mono<? extends Pet> removePhotoById(String id) {
        Query query = new Query(Criteria.where("photos").elemMatch(Criteria.where("_id").is(id)));
        Mono<? extends Pet> pet = mongoTemplate.findOne(query, Pet.class);
        return pet.zipWith(
                pet
                        .flatMapIterable(Pet::getPhotos)
                        .filter(photo -> !photo.getId().equals(id))
                        .collectList(), (pt, photos) -> {
                            pt.setPhotos(photos);
                            return pt;
                        }).flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<Photo> emptyPhoto() {
        return Mono.just(Photo.builder().build());
    }
}

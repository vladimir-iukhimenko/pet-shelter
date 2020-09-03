package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Photo;

@Repository
public interface PhotoRepository extends ReactiveMongoRepository<Photo, String> {
}

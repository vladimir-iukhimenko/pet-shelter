package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefDogBreed;

@Repository
public interface RefDogBreedRepository extends ReactiveMongoRepository<RefDogBreed, String> {
}

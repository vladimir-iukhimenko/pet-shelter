package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefCatBreed;

@Repository
public interface RefCatBreedRepository extends ReactiveMongoRepository<RefCatBreed, String> {
}

package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefShelter;

@Repository
public interface RefShelterRepostiory extends ReactiveMongoRepository<RefShelter, String> {
}

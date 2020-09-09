package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Dog;

@Repository
public interface DogRepository extends PetGenericRepository<Dog> {
}

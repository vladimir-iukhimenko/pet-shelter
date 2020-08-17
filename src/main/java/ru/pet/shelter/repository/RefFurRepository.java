package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefFur;

@Repository
public interface RefFurRepository extends ReactiveMongoRepository<RefFur, String> {
}

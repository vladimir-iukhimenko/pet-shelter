package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefFurColor;

@Repository
public interface RefFurColorRepository extends ReactiveMongoRepository<RefFurColor, String> {
}
package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Description;

@Repository
public interface DescriptionRepository extends ReactiveMongoRepository<Description, String> {
}

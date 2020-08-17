package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.Request;

@Repository
public interface RequestRepository extends ReactiveMongoRepository<Request, String> {
}

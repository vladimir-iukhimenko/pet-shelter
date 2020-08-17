package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.RefVaccination;

@Repository
public interface RefVaccinationRepository extends ReactiveMongoRepository<RefVaccination, String> {
}

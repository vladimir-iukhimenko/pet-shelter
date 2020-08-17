package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.pet.shelter.model.PassportVaccination;

@Repository
public interface PassportVaccinationRepository extends ReactiveMongoRepository<PassportVaccination, String> {
}

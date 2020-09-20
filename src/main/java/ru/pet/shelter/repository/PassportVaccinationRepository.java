package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.PassportVaccination;
import ru.pet.shelter.model.Pet;

@Repository
public interface PassportVaccinationRepository {
    Flux<PassportVaccination> findAllPassportVaccination(String id);
    Mono<PassportVaccination> findPassportVaccinationById(String id);
    Mono<? extends Pet> savePassportVaccination(String id, PassportVaccination passportVaccination);
    Mono<? extends Pet> updatePassportVaccination(String id, PassportVaccination passportVaccination);
    Mono<? extends Pet> removePassportVaccinationById(String id);
    Mono<PassportVaccination> emptyPassportVaccination();
}

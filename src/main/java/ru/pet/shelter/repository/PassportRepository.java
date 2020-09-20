package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.model.Pet;

@Repository
public interface PassportRepository {
    Flux<Passport> findAllPassport();
    Mono<Passport> findPassportById(String id);
    Mono<? extends Pet> savePassport(String id, Passport passport);
    Mono<? extends Pet> updatePassport(String id, Passport passport);
    Mono<? extends Pet> removePassportById(String id);
    Mono<Passport> emptyPassport();
}

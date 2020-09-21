package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;

@Repository
public interface PetReadRepository {
    Flux<? extends Pet> findAllPet();
    Mono<? extends Pet> findPetById(String id);
}

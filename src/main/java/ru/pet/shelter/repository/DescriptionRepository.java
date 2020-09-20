package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Description;
import ru.pet.shelter.model.Pet;

@Repository
public interface DescriptionRepository {
    Flux<Description> findAllDescription(String id);
    Mono<Description> findDescriptionById(String id);
    Mono<? extends Pet> saveDescription(String id, Description description);
    Mono<? extends Pet> updateDescription(String id, Description description);
    Mono<? extends Pet> removeDescriptionById(String id);
    Mono<Description> emptyDescription();
}

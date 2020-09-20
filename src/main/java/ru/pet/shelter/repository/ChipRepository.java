package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.model.Pet;

@Repository
public interface ChipRepository {
    Flux<Chip> findAllChip();
    Mono<Chip> findChipById(String id);
    Mono<? extends Pet> saveChip(String id, Chip chip);
    Mono<? extends Pet> updateChip(String id, Chip chip);
    Mono<? extends Pet> removeChipById(String id);
    Mono<Chip> emptyChip();
}

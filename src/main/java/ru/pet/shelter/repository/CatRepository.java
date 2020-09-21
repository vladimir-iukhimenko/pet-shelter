package ru.pet.shelter.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;


@Repository
public interface CatRepository {
    Flux<Cat> findAllCat();
    Mono<Cat> findCatById(String id);
    Mono<Cat> saveCat(Cat cat);
    Mono<Cat> updateCat(Cat cat);
    Mono<Cat> removeCatById(String id);
    Mono<Cat> emptyCat();

}

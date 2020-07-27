package ru.pet.shelter.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;

public interface CatRepository {

    Mono<Cat> findCatById(String id);
    Flux<Cat> findAllCats();
    Mono<Cat> updateCat(Cat cat);

}

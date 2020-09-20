package ru.pet.shelter.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;

public interface GenericPetService<T> {

    Flux<T> findAll();
    Mono<T> findById(String id);
    Mono<? extends Pet> save(String id, T entity);
    Mono<? extends Pet> update(String id, T entity);
    Mono<? extends Pet> removeById(String id);
    Mono<T> empty();

}

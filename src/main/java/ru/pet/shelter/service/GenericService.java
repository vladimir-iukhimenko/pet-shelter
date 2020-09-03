package ru.pet.shelter.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenericService<T> {

    Flux<T> getAll();
    Mono<T> getById(String id);
    Mono<T> save(T entity);
    Mono<T> update(T entity);
    Mono<Void> deleteById(String id);
    Mono<T> empty();

}

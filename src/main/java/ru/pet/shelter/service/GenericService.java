package ru.pet.shelter.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GenericService<T> {

    Flux<T> getAll();
    Mono<T> getById(String id);
    Mono<T> empty();

}

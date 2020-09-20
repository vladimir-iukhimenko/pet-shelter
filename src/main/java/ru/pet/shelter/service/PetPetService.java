package ru.pet.shelter.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;

public class PetPetService implements GenericPetService<Pet> {
    @Override
    public Flux<Pet> getAll() {
        return null;
    }

    @Override
    public Mono<Pet> getById(String id) {
        return null;
    }

    public Mono<Pet> save(Pet entity) {
        return null;
    }

    public Mono<Pet> update(Pet entity) {
        return null;
    }

    public Mono<Void> deleteById(String id) {
        return null;
    }

    @Override
    public Mono<Pet> empty() {
        return null;
    }
}

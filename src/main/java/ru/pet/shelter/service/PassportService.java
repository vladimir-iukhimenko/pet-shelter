package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.*;
import ru.pet.shelter.repository.CatRepository;
import ru.pet.shelter.repository.DogRepository;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Passport")
public class PassportService implements GenericService<Passport> {
    private final CatRepository catRepository;
    private final DogRepository dogRepository;
    private final PetRepository petRepository;

    @Autowired
    public PassportService(CatRepository catRepository, DogRepository dogRepository, PetRepository petRepository) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Passport> getAll() {
        return catRepository.findAll().flatMap(cat -> Mono.justOrEmpty(cat.getPassport()))
                .concatWith(dogRepository.findAll().flatMap(dog -> Mono.justOrEmpty(dog.getPassport())));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Passport> getById(@Parameter(description = "Id объекта") String passportNumber) {
        return getAll()
                .filter(passport -> passport.getNumber().equals(passportNumber))
                .next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Passport> save(Passport entity) {

        return petRepository.findById(entity.getPetId())
                .flatMap(pet -> {
                    switch (pet.getClass().getSimpleName()) {
                        case "Cat": return saveInCat(pet, entity, false);
                        case "Dog": return saveInDog(pet, entity, false);
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ServerWebInputException("Parent entity not found!")));
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Passport> update(Passport entity) {
        return save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return getById(id).flatMap(chip -> petRepository.findById(chip.getPetId())
                .doOnNext(pet -> {
                    switch (pet.getClass().getSimpleName()) {
                        case "Cat": saveInCat(pet, chip, true);
                        case "Dog": saveInDog(pet, chip, true);
                    }
                }).then());
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Passport> empty() {
        return Mono.just(Passport.builder().build());
    }

    private Mono<Passport> saveInCat(Pet pet, Passport passport, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Cat.class)
                .doOnNext(cat -> {if (toDeletion) cat.setPassport(Passport.builder().build()); else cat.setPassport(passport);})
                .flatMap(catRepository::save)
                .map(Cat::getPassport);
    }

    private Mono<Passport> saveInDog(Pet pet, Passport passport, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Dog.class)
                .doOnNext(dog -> {if (toDeletion) dog.setPassport(Passport.builder().build()); else dog.setPassport(passport);})
                .flatMap(dogRepository::save)
                .map(Dog::getPassport);
    }
}

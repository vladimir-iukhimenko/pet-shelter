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
import ru.pet.shelter.repository.PassportVaccinationRepository;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Passport Vaccination")
public class PassportVaccinationService implements GenericService<PassportVaccination> {
    private final CatRepository catRepository;
    private final DogRepository dogRepository;
    private final PetRepository petRepository;

    @Autowired
    public PassportVaccinationService(CatRepository catRepository, DogRepository dogRepository, PetRepository petRepository) {
        this.petRepository = petRepository;
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> getAll() {
        return catRepository.findAll()
                .flatMap(cat -> Mono.justOrEmpty(cat.getPassport()).flatMapIterable(Passport::getPassportVaccinations))
                .concatWith(dogRepository.findAll().flatMap(dog -> Mono.justOrEmpty(dog.getPassport()).flatMapIterable(Passport::getPassportVaccinations)));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<PassportVaccination> getById(@Parameter(description = "Id объекта") String id) {
        return getAll().filter(passportVaccination -> passportVaccination.getId().equals(id)).next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<PassportVaccination> save(PassportVaccination entity) {
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
    public Mono<PassportVaccination> update(PassportVaccination entity) {
        return petRepository.findById(entity.getPetId())
                .flatMap(pet -> {
                    switch (pet.getClass().getSimpleName()) {
                        case "Cat": return updateInCat(pet, entity);
                        case "Dog": return updateInDog(pet, entity);
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(Mono.error(new ServerWebInputException("Parent entity not found!")));
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return passportVaccinationRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<PassportVaccination> empty() {
        return Mono.just(PassportVaccination.builder().build());
    }

    private Mono<PassportVaccination> saveInCat(Pet pet, PassportVaccination passportVaccination, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Cat.class)
                .doOnNext(cat -> Mono.justOrEmpty(cat.getPassport()).zipWith(
                        Mono.just(pet)
                        .cast(Cat.class)
                        .flatMap(thisCat -> Mono.justOrEmpty(thisCat.getPassport()))
                        .flatMapIterable(Passport::getPassportVaccinations)
                        .concatWith(Mono.just(passportVaccination)).collectList(),
                        (pass, vaccinate) -> {
                            pass.setPassportVaccinations(vaccinate);
                            cat.setPassport(pass);
                            return cat;
                        }
                ))

    }

    private Mono<PassportVaccination> saveInDog(Pet pet, PassportVaccination passportVaccination, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Dog.class)
                .doOnNext(dog -> {if (toDeletion) dog.setChip(Chip.builder().build()); else dog.setChip(chip);})
                .flatMap(dogRepository::save)
                .map(Dog::getChip);
    }
}

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
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.repository.CatRepository;
import ru.pet.shelter.repository.DogRepository;
import ru.pet.shelter.repository.PetRepository;


@Service
@Tag(name = "Chip")
public class ChipService implements GenericService<Chip> {
    private final CatRepository catRepository;
    private final DogRepository dogRepository;
    private final PetRepository petRepository;

    @Autowired
    public ChipService(CatRepository catRepository, DogRepository dogRepository, PetRepository petRepository) {
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Chip> getAll() {
        return catRepository.findAll().flatMap(cat -> Mono.justOrEmpty(cat.getChip()))
                .concatWith(dogRepository.findAll().flatMap(dog -> Mono.justOrEmpty(dog.getChip())));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Chip> getById(@Parameter(description = "Id объекта") String chipNumber) {
        return getAll()
                .filter(chip -> chip.getChipNumber().equals(chipNumber))
                .next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Chip> save(Chip entity) {
        petRepository.findById(entity.getPetId())
                .flatMap(pet -> {
                    switch (pet.getClass().getSimpleName()) {
                        case "Cat": return saveInCat(pet, entity, false);
                        case "Dog": return saveInDog(pet, entity, false);
                    }
                })
                .switchIfEmpty(Mono.error(new ServerWebInputException("Parent entity not found!")));
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Chip> update(Chip entity) {
        return save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта", required = true) String id) {
        return getById(id)
                .flatMap(chip -> petRepository.findById(chip.getPetId()))
                .flatMap(pet -> {
                    switch (pet.getClass().getSimpleName()) {
                        case "Cat": return saveInCat(pet, chip, true);
                        case "Dog": return saveInDog(pet, entity, false);
                    }
                })
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Chip> empty() {
        return Mono.just(Chip.builder().build());
    }

    private Mono<Chip> saveInCat(Pet pet, Chip chip, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Cat.class)
                .doOnNext(cat -> {if (toDeletion) cat.setChip(Chip.builder().build()); else cat.setChip(chip);})
                .flatMap(catRepository::save)
                .map(Cat::getChip);
    }

    private Mono<Chip> saveInDog(Pet pet, Chip chip, boolean toDeletion) {
        return Mono.just(pet)
                .cast(Dog.class)
                .doOnNext(dog -> {if (toDeletion) dog.setChip(Chip.builder().build()); else dog.setChip(chip);})
                .flatMap(dogRepository::save)
                .map(Dog::getChip);
    }

}

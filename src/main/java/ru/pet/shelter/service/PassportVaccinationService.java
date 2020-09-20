package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.*;
import ru.pet.shelter.repository.CatRepository;
import ru.pet.shelter.repository.DogRepository;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Passport Vaccination")
public class PassportVaccinationService implements GenericPetService<PassportVaccination> {
    private final PetRepository petRepository;
    private final CatRepository catRepository;
    private final DogRepository dogRepository;

    @Autowired
    public PassportVaccinationService(PetRepository petRepository, CatRepository catRepository, DogRepository dogRepository) {
        this.petRepository = petRepository;
        this.catRepository = catRepository;
        this.dogRepository = dogRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> findAll() {
        return catRepository.findAll()
                .flatMap(cat -> Mono.justOrEmpty(cat.getPassport()).flatMapIterable(Passport::getPassportVaccinations))
                .concatWith(dogRepository.findAll().flatMap(dog -> Mono.justOrEmpty(dog.getPassport()).flatMapIterable(Passport::getPassportVaccinations)));
    }

    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> findAllByPetId(String id) {
        return petRepository.findAllPassportVaccination(id);
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<PassportVaccination> findById(@Parameter(description = "Id объекта") String id) {
        return petRepository.findPassportVaccinationById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(String id, PassportVaccination entity) {
        return petRepository.savePassportVaccination(id, entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<? extends Pet> update(String id, PassportVaccination entity) {
        return petRepository.updatePassportVaccination(id, entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(description = "Id объекта") String id) {
        return petRepository.removePassportVaccinationById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<PassportVaccination> empty() {
        return petRepository.emptyPassportVaccination();
    }

}

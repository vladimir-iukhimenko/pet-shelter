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
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Passport")
public class PassportService implements GenericPetService<Passport> {
    private final PetRepository petRepository;

    @Autowired
    public PassportService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Passport> findAll() {
        return petRepository.findAllPassport();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Passport> findById(@Parameter(description = "Id объекта") String passportNumber) {
        return petRepository.findPassportById(passportNumber);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(String petId, Passport entity) {
        return petRepository.savePassport(petId, entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<? extends Pet> update(String petId,Passport entity) {
        return petRepository.updatePassport(petId, entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(description = "Id объекта") String id) {
        return petRepository.removePassportById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Passport> empty() {
        return petRepository.emptyPassport();
    }

}

package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.*;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Passport", description = "Паспорты")
public class PassportService implements GenericPetService<Passport> {
    private final PetRepository petRepository;

    @Autowired
    public PassportService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(operationId = "findAllPassports", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Passport> findAll() {
        return petRepository.findAllPassport();
    }

    @Override
    @Operation(operationId = "findPassportById", summary = "Возвращает объект по Id")
    public Mono<Passport> findById(@Parameter(in = ParameterIn.PATH, description = "Id паспорта", required = true) String passportNumber) {
        return petRepository.findPassportById(passportNumber);
    }

    @Override
    @Operation(operationId = "savePassport", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                    @RequestBody(required = true) Passport entity) {
        return petRepository.savePassport(petId, entity);
    }

    @Override
    @Operation(operationId = "updatePassport", summary = "Обновляет объект")
    public Mono<? extends Pet> update(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                      @RequestBody(required = true) Passport entity) {
        return petRepository.updatePassport(petId, entity);
    }

    @Override
    @Operation(operationId = "deletePassport", summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(in = ParameterIn.PATH, description = "Id паспорта", required = true) String id) {
        return petRepository.removePassportById(id);
    }

    @Override
    @Operation(operationId = "getEmptyPassport", summary = "Возвращает пустой объект - структуру")
    public Mono<Passport> empty() {
        return petRepository.emptyPassport();
    }

}

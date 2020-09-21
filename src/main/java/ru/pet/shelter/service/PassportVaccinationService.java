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
@Tag(name = "Passport Vaccination", description = "Вакцины")
public class PassportVaccinationService implements GenericPetService<PassportVaccination> {
    private final PetRepository petRepository;

    @Autowired
    public PassportVaccinationService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(operationId = "findAllPassportVaccinations", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> findAll() {
        return petRepository.findAllCat()
                .flatMap(cat -> Mono.justOrEmpty(cat.getPassport()).flatMapIterable(Passport::getPassportVaccinations))
                .concatWith(petRepository.findAllDog().flatMap(dog -> Mono.justOrEmpty(dog.getPassport()).flatMapIterable(Passport::getPassportVaccinations)));
    }

    @Operation(operationId = "findAllPassportVaccinationsByPetId", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> findAllByPetId(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id) {
        return petRepository.findAllPassportVaccination(id);
    }

    @Override
    @Operation(operationId = "findPassportVaccinationById", summary = "Возвращает объект по Id")
    public Mono<PassportVaccination> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true)  String id) {
        return petRepository.findPassportVaccinationById(id);
    }

    @Override
    @Operation(operationId = "savePassportVaccination", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id,
                                    @RequestBody(required = true) PassportVaccination entity) {
        return petRepository.savePassportVaccination(id, entity);
    }

    @Override
    @Operation(operationId = "updatePassportVaccination", summary = "Обновляет объект")
    public Mono<? extends Pet> update(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id,
                                      @RequestBody(required = true) PassportVaccination entity) {
        return petRepository.updatePassportVaccination(id, entity);
    }

    @Override
    @Operation(operationId = "deletePassportVaccination", summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return petRepository.removePassportVaccinationById(id);
    }

    @Override
    @Operation(operationId = "getEmptyPassportVaccination", summary = "Возвращает пустой объект - структуру")
    public Mono<PassportVaccination> empty() {
        return petRepository.emptyPassportVaccination();
    }

}

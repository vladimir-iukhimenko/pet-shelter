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
import ru.pet.shelter.model.Shelter;
import ru.pet.shelter.repository.ShelterRepository;

@Service
@Tag(name = "Shelter", description = "Приюты")
public class ShelterService {
    private final ShelterRepository shelterRepository;

    @Autowired
    public ShelterService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Operation(operationId = "findAllShelters", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Shelter> findAll() {
        return shelterRepository.findAll();
    }

    @Operation(operationId = "findShelterById", summary = "Возвращает объект по Id")
    public Mono<Shelter> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return shelterRepository.findById(id);
    }

    @Operation(operationId = "saveShelter", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Shelter> save(@RequestBody(required = true) Shelter entity) {
        return shelterRepository.save(entity);
    }

    @Operation(operationId = "updateShelter", summary = "Обновляет объект")
    public Mono<Shelter> update(@RequestBody(required = true) Shelter entity) {
        return shelterRepository.save(entity);
    }

    @Operation(operationId = "deleteShelter", summary = "Удаляет объект")
    public Mono<Void> removeById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return shelterRepository.deleteById(id);
    }

    @Operation(operationId = "getEmptyShelter", summary = "Возвращает пустой объект - структуру")
    public Mono<Shelter> empty() {
        return Mono.just(Shelter.builder().build());
    }
}

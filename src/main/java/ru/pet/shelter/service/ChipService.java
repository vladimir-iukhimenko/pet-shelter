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
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.repository.PetRepository;


@Service
@Tag(name = "Chip", description = "Чипы")
public class ChipService implements GenericPetService<Chip> {
    private final PetRepository petRepository;

    @Autowired
    public ChipService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(operationId = "findAllChips", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Chip> findAll() {
        return petRepository.findAllChip();
    }

    @Override
    @Operation(operationId = "findChipById", summary = "Возвращает объект по Id")
    public Mono<Chip> findById(@Parameter(in = ParameterIn.PATH, description = "Id чипа", required = true) String chipNumber) {
        return petRepository.findChipById(chipNumber);
    }

    @Override
    @Operation(operationId = "saveChip", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                    @RequestBody(required = true) Chip entity) {
        return petRepository.saveChip(petId, entity);
    }

    @Override
    @Operation(operationId = "updateChip", summary = "Обновляет объект")
    public Mono<? extends Pet> update(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                      @RequestBody(required = true) Chip entity) {
        return petRepository.updateChip(petId, entity);
    }

    @Override
    @Operation(operationId = "deleteChip", summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(in = ParameterIn.PATH, description = "Id чипа", required = true) String id) {
        return petRepository.removeChipById(id);
    }

    @Override
    @Operation(operationId = "getEmptyChip", summary = "Возвращает пустой объект-структуру")
    public Mono<Chip> empty() {
        return petRepository.emptyChip();
    }
}

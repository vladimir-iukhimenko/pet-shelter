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
import ru.pet.shelter.model.Description;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Description", description = "Описания животных")
public class DescriptionService implements GenericPetService<Description> {
    private final PetRepository petRepository;

    @Autowired
    public DescriptionService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(operationId = "findAllDescriptions", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> findAll() {
        return petRepository.findAllPet().flatMap(pet -> Flux.fromIterable(pet.getDescription()));
    }

    @Operation(operationId = "findAllDescriptionsByPetId", summary = "Возвращает все описания по Id животного", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> findAllByPetId(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id) {
        return petRepository.findAllDescription(id);
    }

    @Override
    @Operation(operationId = "findDescriptionById", summary = "Возвращает объект по Id")
    public Mono<Description> findById(@Parameter(in = ParameterIn.PATH, description = "Id описания", required = true) String id) {
        return petRepository.findDescriptionById(id);
    }

    @Override
    @Operation(operationId = "saveDescription", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                    @RequestBody(required = true) Description entity) {
        return petRepository.saveDescription(petId, entity);
    }

    @Override
    @Operation(operationId = "updateDescription", summary = "Обновляет объект")
    public Mono<? extends Pet> update(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String petId,
                                      @RequestBody(required = true) Description entity) {
        return petRepository.updateDescription(petId, entity);
    }

    @Override
    @Operation(operationId = "deleteDescription",summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(in = ParameterIn.PATH, description = "Id описания", required = true) String id) {
        return petRepository.removeChipById(id);
    }

    @Override
    @Operation(operationId = "getEmptyDescription", summary = "Возвращает пустой объект-структуру")
    public Mono<Description> empty() {
        return petRepository.emptyDescription();
    }
}

package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Description")
public class DescriptionService implements GenericPetService<Description> {
    private final PetRepository petRepository;

    @Autowired
    public DescriptionService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> findAll() {
        return petRepository.findAll().flatMap(pet -> Flux.fromIterable(pet.getDescription()));
    }

    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> findAllByPetId(String id) {
        return petRepository.findAllDescription(id);
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Description> findById(@Parameter(description = "Id объекта") String id) {
        return petRepository.findDescriptionById(id);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(String petId, Description entity) {
        return petRepository.saveDescription(petId, entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<? extends Pet> update(String petId, Description entity) {
        return petRepository.updateDescription(petId, entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(description = "Id объекта") String id) {
        return petRepository.removeChipById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Description> empty() {
        return petRepository.emptyDescription();
    }
}

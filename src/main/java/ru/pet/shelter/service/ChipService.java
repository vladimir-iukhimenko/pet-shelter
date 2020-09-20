package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@Tag(name = "Chip")
public class ChipService implements GenericPetService<Chip> {
    private final PetRepository petRepository;

    @Autowired
    public ChipService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Chip> findAll() {
        return petRepository.findAllChip();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Chip> findById(@Parameter(description = "Id объекта") String chipNumber) {
        return petRepository.findChipById(chipNumber);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(String petId, Chip entity) {
        return petRepository.saveChip(petId, entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<? extends Pet> update(String petId, Chip entity) {
        return petRepository.updateChip(petId, entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(description = "Id объекта", required = true) String id) {
        return petRepository.removeChipById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Chip> empty() {
        return petRepository.emptyChip();
    }
}

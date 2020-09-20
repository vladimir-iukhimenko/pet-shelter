package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Shelter;
import ru.pet.shelter.repository.ShelterRepository;

@Service
@Tag(name = "Ref Shelter")
public class ShelterPetService implements GenericPetService<Shelter> {
    private final ShelterRepository shelterRepository;

    @Autowired
    public ShelterPetService(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Shelter> getAll() {
        return shelterRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Shelter> getById(@Parameter(description = "Id объекта") String id) {
        return shelterRepository.findById(id);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Shelter> save(Shelter entity) {
        return shelterRepository.save(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Shelter> update(Shelter entity) {
        return shelterRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return shelterRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Shelter> empty() {
        return Mono.just(Shelter.builder().build());
    }
}

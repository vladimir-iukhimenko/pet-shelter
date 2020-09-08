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
import ru.pet.shelter.repository.RefShelterRepository;

@Service
@Tag(name = "Ref Shelter")
public class RefShelterService implements GenericService<Shelter> {
    private final RefShelterRepository refShelterRepository;

    @Autowired
    public RefShelterService(RefShelterRepository refShelterRepository) {
        this.refShelterRepository = refShelterRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Shelter> getAll() {
        return refShelterRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Shelter> getById(@Parameter(description = "Id объекта") String id) {
        return refShelterRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Shelter> save(Shelter entity) {
        return refShelterRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Shelter> update(Shelter entity) {
        return refShelterRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return refShelterRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Shelter> empty() {
        return Mono.just(Shelter.builder().build());
    }
}

package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefDogBreed;
import ru.pet.shelter.repository.RefDogBreedRepository;

@Service
@Tag(name = "Ref Dog Breed")
public class RefDogBreedService implements GenericService<RefDogBreed> {
    private final RefDogBreedRepository refDogBreedRepository;

    @Autowired
    public RefDogBreedService(RefDogBreedRepository refDogBreedRepository) {
        this.refDogBreedRepository = refDogBreedRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<RefDogBreed> getAll() {
        return refDogBreedRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<RefDogBreed> getById(@Parameter(description = "Id объекта") String id) {
        return refDogBreedRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<RefDogBreed> save(RefDogBreed entity) {
        return refDogBreedRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<RefDogBreed> update(RefDogBreed entity) {
        return refDogBreedRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return refDogBreedRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<RefDogBreed> empty() {
        return Mono.just(new RefDogBreed());
    }
}

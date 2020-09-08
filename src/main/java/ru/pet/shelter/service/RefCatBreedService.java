package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefCatBreed;
import ru.pet.shelter.repository.RefCatBreedRepository;

@Service
@Tag(name = "Ref Cat Breed")
public class RefCatBreedService implements GenericService<RefCatBreed> {
    private final RefCatBreedRepository refCatBreedRepository;

    @Autowired
    public RefCatBreedService(RefCatBreedRepository refCatBreedRepository) {
        this.refCatBreedRepository = refCatBreedRepository;
    }
    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<RefCatBreed> getAll() {
        return refCatBreedRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<RefCatBreed> getById(String id) {
        return refCatBreedRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<RefCatBreed> save(RefCatBreed entity) {
        return refCatBreedRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<RefCatBreed> update(RefCatBreed entity) {
        return refCatBreedRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return refCatBreedRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<RefCatBreed> empty() {
        return Mono.just(new RefCatBreed());
    }
}

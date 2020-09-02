package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.repository.DogRepository;

@Service
@Tag(name = "Dog")
public class DogService implements GenericService<Dog> {
    private final DogRepository dogRepository;

    @Autowired
    public DogService(DogRepository dogRepository) {
        this.dogRepository = dogRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Dog> getAll() {
        return dogRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Dog> getById(String id) {
        return dogRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<Dog> save(Dog entity) {
        return dogRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Dog> update(Dog entity) {
        return dogRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return dogRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Dog> empty() {
        return Mono.just(new Dog());
    }
}

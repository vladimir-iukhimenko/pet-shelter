package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
public class DogPetService implements GenericPetService<Dog> {

    private final DogRepository dogRepository;
    private final ShelterPetService shelterService;

    @Autowired
    public DogPetService(DogRepository dogRepository, ShelterPetService shelterService) {
        this.dogRepository = dogRepository;
        this.shelterService = shelterService;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Dog> getAll() {
        return dogRepository.findAll()
                .flatMap(dog -> Mono.just(dog)
                        .zipWith(shelterService.getById(dog.getShelterId()),
                                (dg,sr) -> {
                                    dg.setShelter(sr);
                                    return dg;
                                })
                );
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Dog> getById(@Parameter(description = "Id объекта") String id) {
        return dogRepository.findById(id);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Dog> save(Dog entity) {
        return dogRepository.save(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Dog> update(Dog entity) {
        return dogRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return dogRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Dog> empty() {
        return Mono.just(Dog.builder().build());
    }
}

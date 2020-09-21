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
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Dog", description = "Собаки")
public class DogService {

    private final PetRepository petRepository;
    private final ShelterService shelterService;

    @Autowired
    public DogService(PetRepository petRepository, ShelterService shelterService) {
        this.petRepository = petRepository;
        this.shelterService = shelterService;
    }

    @Operation(operationId = "findAllDogs", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Dog> findAll() {
        return petRepository.findAllDog()
                .flatMap(dog -> Mono.just(dog)
                        .zipWith(shelterService.findById(dog.getShelterId()),
                                (dg,sr) -> {
                                    dg.setShelter(sr);
                                    return dg;
                                })
                );
    }

    @Operation(operationId = "findDogById", summary = "Возвращает объект по Id")
    public Mono<Dog> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return petRepository.findDogById(id);
    }

    @Operation(operationId = "saveDog", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Dog> save(@RequestBody(required = true) Dog entity) {
        return petRepository.saveDog(entity);
    }

    @Operation(operationId = "updateDog", summary = "Обновляет объект")
    public Mono<Dog> update(@RequestBody(required = true) Dog entity) {
        return petRepository.saveDog(entity);
    }

    @Operation(operationId = "deleteDog", summary = "Удаляет объект")
    public Mono<Dog> removeById(@Parameter(description = "Id объекта") String id) {
        return petRepository.removeDogById(id);
    }

    @Operation(operationId = "getEmptyDog", summary = "Возвращает пустой объект-структуру")
    public Mono<Dog> empty() {
        return petRepository.emptyDog();
    }
}

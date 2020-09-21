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
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Cat", description = "Коты")
public class CatService {

    private final PetRepository petRepository;
    private final ShelterService shelterService;

    @Autowired
    public CatService(PetRepository petRepository, ShelterService shelterService) {
        this.petRepository = petRepository;
        this.shelterService = shelterService;
    }

    @Operation(operationId = "findAllCats", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Cat> findAll() {

        return petRepository.findAllCat()
                .flatMap(cat -> Mono.just(cat)
                        .zipWith(shelterService.findById(cat.getShelterId()),
                                (ct,sr) -> {
                                    ct.setShelter(sr);
                                    return ct;
                                })
                );
    }

    @Operation(operationId = "findCatById", summary = "Возвращает объект по Id")
    public Mono<Cat> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return petRepository.findCatById(id);
    }

    @Operation(operationId = "saveCat", summary = "Сохраняет объект",
            responses = @ApiResponse(responseCode = "201", description = "Объект создан"))
    public Mono<Cat> save(@RequestBody(required = true) Cat entity) {
        return petRepository.saveCat(entity);
    }

    @Operation(operationId = "updateCat", summary = "Обновляет объект")
    public Mono<Cat> update(@RequestBody(required = true) Cat entity) {
         return petRepository.updateCat(entity);
    }

    @Operation(operationId = "deleteCat", summary = "Удаляет объект")
    public Mono<Cat> removeById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
         return petRepository.removeCatById(id);
    }

    @Operation(operationId = "getEmptyCat", summary = "Возвращает пустой объект-структуру")
    public Mono<Cat> empty() {
        return petRepository.emptyCat();
    }
}

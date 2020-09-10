package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.repository.CatRepository;

@Service
@Tag(name = "Cat")
public class CatService implements GenericService<Cat> {

    private final CatRepository catRepository;
    private final ShelterService shelterService;

    @Autowired
    public CatService(CatRepository catRepository, ShelterService shelterService) {
        this.catRepository = catRepository;
        this.shelterService = shelterService;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Cat> getAll() {

        return catRepository.findAll()
                .flatMap(cat -> Mono.just(cat)
                        .zipWith(shelterService.getById(cat.getShelterId()),
                                (ct,sr) -> {
                            ct.setShelter(sr);
                            return ct;
                                })
                );
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Cat> getById(@Parameter(description = "Id объекта", required = true) String id) {
        return catRepository.findById(id);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Cat> save(Cat entity) {
        return catRepository.insert(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Cat> update(Cat entity) {
         return catRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта", required = true) String id) {
         return catRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Cat> empty() {
        return Mono.just(new Cat());
    }
}

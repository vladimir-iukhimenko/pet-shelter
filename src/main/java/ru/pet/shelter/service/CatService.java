package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
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
public class CatService implements GenericService<Cat>{

    private CatRepository catRepository;

    @Autowired
    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }

    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Cat> getAll() {
        return catRepository.findAll();
    }

    @Operation(summary = "Возвращает объект по Id")
    public Mono<Cat> getById(String id) {
        return catRepository.findById(id);
    }

    @Operation(summary = "Сохраняет объект")
    public Mono<Cat> save(Cat entity) {
        return catRepository.save(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Cat> update(Cat entity) {
         return catRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
         return catRepository.deleteById(id);
    }

    @Operation(summary = "Возвращает пустой объект")
    public Mono<Cat> empty() {
        return Mono.just(new Cat());
    }
}

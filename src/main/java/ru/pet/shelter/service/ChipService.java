package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Chip;

@Service
@Tag(name = "Chip")
public class ChipService implements GenericService<Chip> {
    private final CatService catService;
    private final DogService dogService;

    @Autowired
    public ChipService(CatService catService, DogService dogService) {
        this.catService = catService;
        this.dogService = dogService;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Chip> getAll() {
        return catService.getAll().flatMap(cat -> Mono.justOrEmpty(cat.getChip()))
                .concatWith(dogService.getAll().flatMap(dog -> Mono.justOrEmpty(dog.getChip())));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Chip> getById(@Parameter(description = "Id объекта") String chipNumber) {
        return getAll()
                .filter(chip -> chip.getChipNumber().equals(chipNumber))
                .next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Chip> save(Chip entity) {
        return catRepository.insert(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Chip> update(Chip entity) {
        return catRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта", required = true) String id) {
        return catRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Chip> empty() {
        return Mono.just(new Chip());
    }
}

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
import ru.pet.shelter.repository.ChipRepository;

@Service
@Tag(name = "Chip")
public class ChipService implements GenericService<Chip> {
    private final ChipRepository chipRepository;
    private final CatService catService;
    private final DogService dogService;

    @Autowired
    public ChipService(ChipRepository chipRepository, CatService catService, DogService dogService) {
        this.chipRepository = chipRepository;
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

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Chip> empty() {
        return Mono.just(new Chip());
    }
}

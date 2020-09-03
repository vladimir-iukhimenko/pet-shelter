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

    @Autowired
    public ChipService(ChipRepository chipRepository) {
        this.chipRepository = chipRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Chip> getAll() {
        return chipRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Chip> getById(@Parameter(description = "Id объекта") String id) {
        return chipRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Chip> save(Chip entity) {
        return chipRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Chip> update(Chip entity) {
        return chipRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return chipRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Chip> empty() {
        return Mono.just(new Chip());
    }
}

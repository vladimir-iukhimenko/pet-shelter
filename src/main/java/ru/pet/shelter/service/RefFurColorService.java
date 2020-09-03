package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefFurColor;
import ru.pet.shelter.repository.RefFurColorRepository;

@Service
@Tag(name = "Ref Fur Color")
public class RefFurColorService implements GenericService<RefFurColor> {
    private final RefFurColorRepository refFurColorRepository;

    @Autowired
    public RefFurColorService(RefFurColorRepository refFurColorRepository) {
        this.refFurColorRepository = refFurColorRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<RefFurColor> getAll() {
        return refFurColorRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<RefFurColor> getById(@Parameter(description = "Id объекта") String id) {
        return refFurColorRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<RefFurColor> save(RefFurColor entity) {
        return refFurColorRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<RefFurColor> update(RefFurColor entity) {
        return refFurColorRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return refFurColorRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<RefFurColor> empty() {
        return Mono.just(new RefFurColor());
    }
}

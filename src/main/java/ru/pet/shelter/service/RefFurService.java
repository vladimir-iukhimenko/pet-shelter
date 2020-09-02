package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefFur;
import ru.pet.shelter.repository.RefFurRepository;

@Service
@Tag(name = "Ref Fur")
public class RefFurService implements GenericService<RefFur> {
    private final RefFurRepository refFurRepository;

    @Autowired
    public RefFurService(RefFurRepository refFurRepository) {
        this.refFurRepository = refFurRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<RefFur> getAll() {
        return refFurRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<RefFur> getById(String id) {
        return refFurRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<RefFur> save(RefFur entity) {
        return refFurRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<RefFur> update(RefFur entity) {
        return refFurRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return refFurRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<RefFur> empty() {
        return Mono.just(new RefFur());
    }
}

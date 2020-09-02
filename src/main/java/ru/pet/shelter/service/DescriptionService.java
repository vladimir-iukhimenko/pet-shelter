package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Description;
import ru.pet.shelter.repository.DescriptionRepository;

@Service
@Tag(name = "Description")
public class DescriptionService implements GenericService<Description> {
    private final DescriptionRepository descriptionRepository;

    @Autowired
    public DescriptionService(DescriptionRepository descriptionRepository) {
        this.descriptionRepository = descriptionRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> getAll() {
        return descriptionRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Description> getById(String id) {
        return descriptionRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<Description> save(Description entity) {
        return descriptionRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Description> update(Description entity) {
        return descriptionRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return descriptionRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Description> empty() {
        return Mono.just(new Description());
    }
}
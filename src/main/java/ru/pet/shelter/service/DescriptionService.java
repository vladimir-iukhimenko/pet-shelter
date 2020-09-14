package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Description;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.repository.DescriptionRepository;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Description")
public class DescriptionService implements GenericService<Description> {
    private final DescriptionRepository descriptionRepository;
    private final PetRepository petRepository;

    @Autowired
    public DescriptionService(DescriptionRepository descriptionRepository, PetRepository petRepository) {
        this.descriptionRepository = descriptionRepository;
        this.petRepository = petRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Description> getAll() {
        return petRepository.findAll().flatMap(pet -> Flux.fromIterable(pet.getDescription()));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Description> getById(@Parameter(description = "Id объекта") String id) {
        return getAll().filter(description -> description.getId().equals(id)).next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Description> save(Description entity) {
        return petRepository.findAll()
                .filter(pet -> pet.getId().equals(entity.getPetId()))
                .next()
                .doOnNext(pet -> pet.getDescription().add(entity))
                .flatMap(petRepository::save)
                .flatMapIterable(Pet::getDescription)
                .last()
                .switchIfEmpty(Mono.empty());
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Description> update(Description entity) {
        return descriptionRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return descriptionRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Description> empty() {
        return Mono.just(Description.builder().build());
    }
}

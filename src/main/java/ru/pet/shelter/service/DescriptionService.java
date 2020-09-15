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
    private final PetRepository petRepository;

    @Autowired
    public DescriptionService(PetRepository petRepository) {
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
        return petRepository.findById(entity.getPetId())
                .flatMap(pet -> Mono.just(pet)
                        .zipWith(
                                petRepository
                                        .findById(entity.getPetId())
                                        .flatMapIterable(Pet::getDescription)
                                        .filter(description -> !(description.getId().equals(entity.getId())))
                                        .concatWith(Mono.just(entity)).collectList(),
                                (pt, description) -> {
                                    pt.setDescription(description);
                                    return pt;
                                })
                )
                .flatMap(petRepository::save)
                .flatMapIterable(Pet::getDescription)
                .filter(description -> description.getId().equals(entity.getId()))
                .next();
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return getById(id)
                .flatMap(description -> petRepository.findById(description.getPetId()))
                .flatMap(pet -> Mono.just(pet)
                        .zipWith(
                                petRepository
                                        .findById(pet.getId())
                                        .flatMapIterable(Pet::getDescription)
                                        .filter(description -> !(description.getId().equals(id)))
                                        .collectList(),
                                (pt, description) -> {
                                    pt.setDescription(description);
                                    return pt;
                                })
                )
                .flatMap(petRepository::save)
                .then();
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Description> empty() {
        return Mono.just(Description.builder().build());
    }
}

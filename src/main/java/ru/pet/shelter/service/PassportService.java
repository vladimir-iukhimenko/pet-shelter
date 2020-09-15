package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.repository.PassportRepository;

@Service
@Tag(name = "Passport")
public class PassportService implements GenericService<Passport> {
    private final CatService catService;
    private final DogService dogService;

    @Autowired
    public PassportService(CatService catService, DogService dogService) {
        this.catService = catService;
        this.dogService = dogService;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Passport> getAll() {
        return catService.getAll().flatMap(cat -> Mono.justOrEmpty(cat.getPassport()))
                .concatWith(dogService.getAll().flatMap(dog -> Mono.justOrEmpty(dog.getPassport())));
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Passport> getById(@Parameter(description = "Id объекта") String passportNumber) {
        return getAll()
                .filter(passport -> passport.getNumber().equals(passportNumber))
                .next();
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Passport> save(Passport entity) {
        return passportRepository.save(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Passport> update(Passport entity) {
        return passportRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return passportRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Passport> empty() {
        return Mono.just(Passport.builder().build());
    }
}

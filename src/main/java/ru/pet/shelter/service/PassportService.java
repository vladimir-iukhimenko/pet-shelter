package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
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
    private final PassportRepository passportRepository;

    @Autowired
    public PassportService(PassportRepository passportRepository) {
        this.passportRepository = passportRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Passport> getAll() {
        return passportRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Passport> getById(String id) {
        return passportRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<Passport> save(Passport entity) {
        return passportRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Passport> update(Passport entity) {
        return passportRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return passportRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Passport> empty() {
        return Mono.just(new Passport());
    }
}

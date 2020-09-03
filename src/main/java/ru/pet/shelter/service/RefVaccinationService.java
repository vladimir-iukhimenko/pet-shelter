package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefVaccination;
import ru.pet.shelter.repository.RefVaccinationRepository;

@Service
@Tag(name = "Ref Vaccination")
public class RefVaccinationService implements GenericService<RefVaccination> {
    private final RefVaccinationRepository refVaccinationRepository;

    @Autowired
    public RefVaccinationService(RefVaccinationRepository refVaccinationRepository) {
        this.refVaccinationRepository = refVaccinationRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<RefVaccination> getAll() {
        return refVaccinationRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<RefVaccination> getById(@Parameter(description = "Id объекта") String id) {
        return refVaccinationRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<RefVaccination> save(RefVaccination entity) {
        return refVaccinationRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<RefVaccination> update(RefVaccination entity) {
        return refVaccinationRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return refVaccinationRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<RefVaccination> empty() {
        return Mono.just(new RefVaccination());
    }
}

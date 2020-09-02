package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.PassportVaccination;
import ru.pet.shelter.repository.PassportVaccinationRepository;

@Service
@Tag(name = "Passport Vaccination")
public class PassportVaccinationService implements GenericService<PassportVaccination> {
    private final PassportVaccinationRepository passportVaccinationRepository;

    @Autowired
    public PassportVaccinationService(PassportVaccinationRepository passportVaccinationRepository) {
        this.passportVaccinationRepository = passportVaccinationRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<PassportVaccination> getAll() {
        return passportVaccinationRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<PassportVaccination> getById(String id) {
        return passportVaccinationRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<PassportVaccination> save(PassportVaccination entity) {
        return passportVaccinationRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<PassportVaccination> update(PassportVaccination entity) {
        return passportVaccinationRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return passportVaccinationRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<PassportVaccination> empty() {
        return Mono.just(new PassportVaccination());
    }
}
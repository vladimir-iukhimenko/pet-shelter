package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Request;
import ru.pet.shelter.repository.RequestRepository;

@Service
@Tag(name = "Request")
public class RequestPetService implements GenericPetService<Request> {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestPetService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Request> getAll() {
        return requestRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Request> getById(@Parameter(description = "Id объекта") String id) {
        return requestRepository.findById(id);
    }

    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Request> save(Request entity) {
        return requestRepository.save(entity);
    }

    @Operation(summary = "Обновляет объект")
    public Mono<Request> update(Request entity) {
        return requestRepository.save(entity);
    }

    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(@Parameter(description = "Id объекта") String id) {
        return requestRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Request> empty() {
        return Mono.just(Request.builder().build());
    }
}

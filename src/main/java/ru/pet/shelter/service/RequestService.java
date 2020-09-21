package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Request;
import ru.pet.shelter.repository.RequestRepository;

@Service
@Tag(name = "Request", description = "Заявки на животных")
public class RequestService {
    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @Operation(operationId = "findAllRequests", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Request> findAll() {
        return requestRepository.findAll();
    }

    @Operation(operationId = "findRequestById", summary = "Возвращает объект по Id")
    public Mono<Request> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return requestRepository.findById(id);
    }

    @Operation(operationId = "saveRequest", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<Request> save(@RequestBody(required = true) Request entity) {
        return requestRepository.save(entity);
    }

    @Operation(operationId = "updateRequest", summary = "Обновляет объект")
    public Mono<Request> update(@RequestBody(required = true) Request entity) {
        return requestRepository.save(entity);
    }

    @Operation(operationId = "deleteRequest", summary = "Удаляет объект")
    public Mono<Void> removeById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return requestRepository.deleteById(id);
    }

    @Operation(operationId = "getEmptyRequest", summary = "Возвращает пустой объект - структуру")
    public Mono<Request> empty() {
        return Mono.just(Request.builder().build());
    }
}

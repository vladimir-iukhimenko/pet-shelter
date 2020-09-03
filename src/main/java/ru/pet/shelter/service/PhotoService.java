package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.repository.PhotoRepository;

@Service
@Tag(name = "Photo")
public class PhotoService implements GenericService<Photo> {
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Photo> getAll() {
        return photoRepository.findAll();
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Photo> getById(String id) {
        return photoRepository.findById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект")
    public Mono<Photo> save(Photo entity) {
        return photoRepository.save(entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<Photo> update(Photo entity) {
        return photoRepository.save(entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<Void> deleteById(String id) {
        return photoRepository.deleteById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Photo> empty() {
        return Mono.just(new Photo());
    }
}

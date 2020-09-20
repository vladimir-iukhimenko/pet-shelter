package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.repository.PhotoRepository;

@Service
@Tag(name = "Photo")
public class PhotoService implements GenericPetService<Photo> {
    private final PhotoRepository photoRepository;

    @Autowired
    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    @Override
    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Photo> findAll() {
        return photoRepository.findAllPhoto();
    }

    @Operation(summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Photo> findAllByPet(String id) {
        return photoRepository.findAllPhotoByPet(id);
    }

    @Override
    @Operation(summary = "Возвращает объект по Id")
    public Mono<Photo> findById(@Parameter(description = "Id объекта") String id) {
        return photoRepository.findPhotoById(id);
    }

    @Override
    @Operation(summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(String id, Photo entity) {
        return photoRepository.savePhoto(id, entity);
    }

    @Override
    @Operation(summary = "Обновляет объект")
    public Mono<? extends Pet> update(String id, Photo entity) {
        return photoRepository.updatePhoto(id, entity);
    }

    @Override
    @Operation(summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(description = "Id объекта") String id) {
        return photoRepository.removePhotoById(id);
    }

    @Override
    @Operation(summary = "Возвращает пустой объект")
    public Mono<Photo> empty() {
        return photoRepository.emptyPhoto();
    }
}

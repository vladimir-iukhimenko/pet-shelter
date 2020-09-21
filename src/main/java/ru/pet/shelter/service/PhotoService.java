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
import ru.pet.shelter.model.Pet;
import ru.pet.shelter.model.Photo;
import ru.pet.shelter.repository.PetRepository;

@Service
@Tag(name = "Photo", description = "Фотографии")
public class PhotoService implements GenericPetService<Photo> {
    private final PetRepository petRepository;

    @Autowired
    public PhotoService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    @Operation(operationId = "findAllPhotos", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Photo> findAll() {
        return petRepository.findAllPhoto();
    }

    @Operation(operationId = "findAllPhotosByPetId", summary = "Возвращает все сущности", responses = {
            @ApiResponse(responseCode = "200", description = "Успешная операция")
    })
    public Flux<Photo> findAllByPetId(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id) {
        return petRepository.findAllPhotoByPet(id);
    }

    @Override
    @Operation(operationId = "findPhotoById", summary = "Возвращает объект по Id")
    public Mono<Photo> findById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return petRepository.findPhotoById(id);
    }

    @Override
    @Operation(operationId = "savePhoto", summary = "Сохраняет объект", responses = {
            @ApiResponse(responseCode = "201", description = "Объект создан")
    })
    public Mono<? extends Pet> save(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id,
                                    @RequestBody(required = true) Photo entity) {
        return petRepository.savePhoto(id, entity);
    }

    @Override
    @Operation(operationId = "updatePhoto", summary = "Обновляет объект")
    public Mono<? extends Pet> update(@Parameter(in = ParameterIn.PATH, description = "Id животного", required = true) String id,
                                      @RequestBody(required = true)Photo entity) {
        return petRepository.updatePhoto(id, entity);
    }

    @Override
    @Operation(operationId = "deletePhoto", summary = "Удаляет объект")
    public Mono<? extends Pet> removeById(@Parameter(in = ParameterIn.PATH, description = "Id объекта", required = true) String id) {
        return petRepository.removePhotoById(id);
    }

    @Override
    @Operation(operationId = "getEmptyPhoto", summary = "Возвращает пустой объект - структуру")
    public Mono<Photo> empty() {
        return petRepository.emptyPhoto();
    }
}

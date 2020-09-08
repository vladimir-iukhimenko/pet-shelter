package ru.pet.shelter.service;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.RefFurColor;
import ru.pet.shelter.repository.RefFurColorRepository;

@Service
@Tag(name = "Ref Fur Color")
public class RefFurColorService implements GenericService<RefFurColor> {
    private final RefFurColorRepository refFurColorRepository;

    @Autowired
    public RefFurColorService(RefFurColorRepository refFurColorRepository) {
        this.refFurColorRepository = refFurColorRepository;
    }

    @Override
    public Flux<RefFurColor> getAll() {
        return refFurColorRepository.findAll();
    }

    @Override
    public Mono<RefFurColor> getById(String id) {
        return refFurColorRepository.findById(id);
    }

    @Override
    public Mono<RefFurColor> save(RefFurColor entity) {
        return refFurColorRepository.save(entity);
    }

    @Override
    public Mono<RefFurColor> update(RefFurColor entity) {
        return refFurColorRepository.save(entity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return refFurColorRepository.deleteById(id);
    }

    @Override
    public Mono<RefFurColor> empty() {
        return Mono.just(new RefFurColor());
    }
}

package ru.pet.shelter.repository;

import org.reactivestreams.Subscriber;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;

@Repository
public interface CatRepository extends ReactiveMongoRepository<Cat, String> {

}

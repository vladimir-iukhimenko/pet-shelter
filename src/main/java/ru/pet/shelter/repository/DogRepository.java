package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.Dog;

@Repository
public interface DogRepository {
    Flux<Dog> findAllDog();
    Mono<Dog> findDogById(String id);
    Mono<Dog> saveDog(Dog dog);
    Mono<Dog> updateDog(Dog dog);
    Mono<Dog> removeDogById(String id);
    Mono<Dog> emptyDog();
}

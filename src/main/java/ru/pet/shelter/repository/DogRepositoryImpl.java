package ru.pet.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Dog;

public class DogRepositoryImpl implements DogRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public DogRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Dog> findAllDog() {
        Query query = new Query(Criteria.where("_class").is("dog"));
        return mongoTemplate.find(query, Dog.class);
    }

    @Override
    public Mono<Dog> findDogById(String id) {
        Query query = new Query(Criteria.where("_class").is("dog")).addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Dog.class);
    }

    @Override
    public Mono<Dog> saveDog(Dog dog) {
        return mongoTemplate.save(dog);
    }

    @Override
    public Mono<Dog> updateDog(Dog dog) {
        return saveDog(dog);
    }

    @Override
    public Mono<Dog> removeDogById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndRemove(query, Dog.class);
    }

    @Override
    public Mono<Dog> emptyDog() {
        return Mono.just(Dog.builder().build());
    }
}

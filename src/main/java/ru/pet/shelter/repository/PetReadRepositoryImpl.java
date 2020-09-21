package ru.pet.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Pet;

public class PetReadRepositoryImpl implements PetReadRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public PetReadRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<? extends Pet> findAllPet() {
        return mongoTemplate.findAll(Pet.class);
    }

    @Override
    public Mono<? extends Pet> findPetById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Pet.class);
    }
}

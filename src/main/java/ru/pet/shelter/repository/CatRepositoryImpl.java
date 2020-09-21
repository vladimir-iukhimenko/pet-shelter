package ru.pet.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;

public class CatRepositoryImpl implements CatRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public CatRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Cat> findAllCat() {
        Query query = new Query(Criteria.where("_class").is("cat"));
        return mongoTemplate.find(query, Cat.class);
    }

    @Override
    public Mono<Cat> findCatById(String id) {
        Query query = new Query(Criteria.where("_class").is("cat")).addCriteria(Criteria.where("_id").is(id));
        return mongoTemplate.findOne(query, Cat.class);
    }

    @Override
    public Mono<Cat> saveCat(Cat cat) {
        return mongoTemplate.save(cat);
    }

    @Override
    public Mono<Cat> updateCat(Cat cat) {
        return saveCat(cat);
    }

    @Override
    public Mono<Cat> removeCatById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoTemplate.findAndRemove(query, Cat.class);
    }

    @Override
    public Mono<Cat> emptyCat() {
        return Mono.just(Cat.builder().build());
    }
}

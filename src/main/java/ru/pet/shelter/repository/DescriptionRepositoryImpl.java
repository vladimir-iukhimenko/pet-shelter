package ru.pet.shelter.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Description;
import ru.pet.shelter.model.Pet;

public class DescriptionRepositoryImpl implements DescriptionRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public DescriptionRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Description> findAllDescription(String id) {
        return mongoTemplate.findById(id, Pet.class).flatMapIterable(Pet::getDescription);
    }

    @Override
    public Mono<Description> findDescriptionById(String id) {
        return mongoTemplate.findAll(Pet.class)
                .flatMapIterable(Pet::getDescription)
                .filter(description -> description.getId().equals(id))
                .next();
    }

    @Override
    public Mono<? extends Pet> saveDescription(String id, Description description) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("description", description);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<? extends Pet> updateDescription(String id, Description description) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Pet.class)
                .zipWith(findAllDescription(id)
                        .filter(dscrptn -> !dscrptn.getId().equals(description.getId()))
                        .concatWithValues(description)
                        .collectList(),
                        (pet, descriptions) -> {
                    pet.setDescription(descriptions);
                    return pet;
                }).flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<? extends Pet> removeDescriptionById(String id) {
        Query query = new Query(Criteria.where("description").elemMatch(Criteria.where("_id").is(id)));
        return mongoTemplate.findOne(query, Pet.class)
                .zipWith(findAllDescription(id)
                                .filter(dscrptn -> !dscrptn.getId().equals(id))
                                .collectList(),
                        (pet, descriptions) -> {
                            pet.setDescription(descriptions);
                            return pet;
                        }).flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<Description> emptyDescription() {
        return Mono.just(Description.builder().build());
    }
}

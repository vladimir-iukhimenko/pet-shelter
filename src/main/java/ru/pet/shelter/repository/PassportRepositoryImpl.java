package ru.pet.shelter.repository;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.model.Passport;
import ru.pet.shelter.model.Pet;

public class PassportRepositoryImpl implements PassportRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public PassportRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Passport> findAllPassport() {
        Query query = new Query(Criteria.where("passport").exists(true));
        return mongoTemplate.find(query, Pet.class).flatMap(pet -> {
            switch (pet.getClass().getSimpleName()) {
                case "Cat": return Mono.just(((Cat)pet).getPassport());
                case "Dog": return Mono.just(((Dog)pet).getPassport());
            }
            return Mono.empty();
        });
    }

    @Override
    public Mono<Passport> findPassportById(String id) {
        return findAllPassport().filter(passport -> passport.getId().equals(id)).next();
    }

    @Override
    public Mono<? extends Pet> savePassport(String id, Passport passport) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("passport", passport);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<? extends Pet> updatePassport(String id, Passport passport) {
        return savePassport(id, passport);
    }

    @Override
    public Mono<? extends Pet> removePassportById(String id) {
        Query query = new Query(Criteria.where("passport._id").is(new ObjectId(id)));
        Update update = new Update().unset("passport");
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<Passport> emptyPassport() {
        return Mono.just(Passport.builder().build());
    }
}

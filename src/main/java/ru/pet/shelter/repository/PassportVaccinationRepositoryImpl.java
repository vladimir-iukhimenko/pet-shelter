package ru.pet.shelter.repository;

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
import ru.pet.shelter.model.PassportVaccination;
import ru.pet.shelter.model.Pet;

public class PassportVaccinationRepositoryImpl implements PassportVaccinationRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public PassportVaccinationRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<PassportVaccination> findAllPassportVaccination(String id) {
        return mongoTemplate.findById(id, Pet.class).flatMapMany(pet -> {
            switch (pet.getClass().getSimpleName()) {
                case "Cat": return Flux.fromIterable(((Cat)pet).getPassport().getPassportVaccinations());
                case "Dog": return Flux.fromIterable(((Dog)pet).getPassport().getPassportVaccinations());
            }
            return Flux.empty();
        });
    }

    @Override
    public Mono<PassportVaccination> findPassportVaccinationById(String id) {
        return mongoTemplate.findAll(Pet.class).flatMap(pet -> {
            switch (pet.getClass().getSimpleName()) {
                case "Cat": return Flux.fromIterable(((Cat)pet).getPassport().getPassportVaccinations());
                case "Dog": return Flux.fromIterable(((Dog)pet).getPassport().getPassportVaccinations());
            }
            return Flux.empty();
        }).filter(vaccination -> vaccination.getId().equals(id)).next();
    }

    @Override
    public Mono<? extends Pet> savePassportVaccination(String id, PassportVaccination passportVaccination) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().addToSet("passport.passportVaccinations", passportVaccination);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<? extends Pet> updatePassportVaccination(String id, PassportVaccination passportVaccination) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, Pet.class)
                .zipWith(findAllPassportVaccination(id)
                        .filter(vaccination -> !vaccination.getId().equals(passportVaccination.getId()))
                        .concatWithValues(passportVaccination)
                        .collectList(),
                        (pet, passportVaccinations) -> {
                            switch (pet.getClass().getSimpleName()) {
                                case "Cat": ((Cat)pet).getPassport().setPassportVaccinations(passportVaccinations); break;
                                case "Dog": ((Dog)pet).getPassport().setPassportVaccinations(passportVaccinations);
                            }
                            return pet;
                        }).flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<? extends Pet> removePassportVaccinationById(String id) {
        Query query = new Query(Criteria.where("passport.passportVaccinations").elemMatch(Criteria.where("_id").is(id)));
        return mongoTemplate.findOne(query, Pet.class)
                .zipWith(findAllPassportVaccination(id)
                                .filter(vaccination -> !vaccination.getId().equals(id))
                                .collectList(),
                        (pet, passportVaccinations) -> {
                            switch (pet.getClass().getSimpleName()) {
                                case "Cat": ((Cat)pet).getPassport().setPassportVaccinations(passportVaccinations); break;
                                case "Dog": ((Dog)pet).getPassport().setPassportVaccinations(passportVaccinations);
                            }
                            return pet;
                        }).flatMap(mongoTemplate::save);
    }

    @Override
    public Mono<PassportVaccination> emptyPassportVaccination() {
        return Mono.just(PassportVaccination.builder().build());
    }
}

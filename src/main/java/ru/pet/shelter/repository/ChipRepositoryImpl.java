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
import ru.pet.shelter.model.Chip;
import ru.pet.shelter.model.Dog;
import ru.pet.shelter.model.Pet;

public class ChipRepositoryImpl implements ChipRepository {
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public ChipRepositoryImpl(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Flux<Chip> findAllChip() {
        Query query = new Query(Criteria.where("chip").exists(true));
        return mongoTemplate.find(query, Pet.class).flatMap(pet -> {
            switch (pet.getClass().getSimpleName()) {
                case "Cat": return Mono.just(((Cat)pet).getChip());
                case "Dog": return Mono.just(((Dog)pet).getChip());
            }
            return Mono.empty();
        });
    }

    @Override
    public Mono<Chip> findChipById(String id) {
        return findAllChip().filter(chip -> chip.getId().equals(id)).next();
    }

    @Override
    public Mono<? extends Pet> saveChip(String id, Chip chip) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update().set("chip", chip);
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<? extends Pet> updateChip(String id, Chip chip) {
        return saveChip(id, chip);
    }

    @Override
    public Mono<? extends Pet> removeChipById(String id) {
        Query query = new Query(Criteria.where("chip._id").is(new ObjectId(id)));
        Update update = new Update().unset("chip");
        return mongoTemplate.findAndModify(query, update, new FindAndModifyOptions().returnNew(true), Pet.class);
    }

    @Override
    public Mono<Chip> emptyChip() {
        return Mono.just(Chip.builder().build());
    }
}

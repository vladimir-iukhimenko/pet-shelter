package ru.pet.shelter.repository.utils;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.bson.Document;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;

public class RegardingInheritanceMongoRepository<T, ID extends Serializable> extends SimpleReactiveMongoRepository<T, ID> {
    private final ReactiveMongoOperations mongoOperations;
    private final MongoEntityInformation<T, ID> entityInformation;
    private final Document classCriteriaDocument;
    private final Criteria classCriteria;

    public RegardingInheritanceMongoRepository(MongoEntityInformation<T, ID> metadata,
                                               ReactiveMongoOperations mongoOperations) {
        super(metadata, mongoOperations);
        this.mongoOperations = mongoOperations;
        this.entityInformation = metadata;

        if (entityInformation.getJavaType().isAnnotationPresent(TypeAlias.class)) {
            classCriteria = where("_class").is(entityInformation.getJavaType().getAnnotation(TypeAlias.class).value());
            classCriteriaDocument = classCriteria.getCriteriaObject();
        } else {
            classCriteriaDocument = new Document();
            classCriteria = null;
        }
    }

    @Override
    public Mono<Long> count() {
        return classCriteria != null ?
                mongoOperations.getCollection(
                entityInformation.getCollectionName()).map(documentMongoCollection -> documentMongoCollection.countDocuments(classCriteriaDocument)).cast(Long.class)
                : super.count();
    }

    @Override
    public Flux<T> findAll() {
        System.out.println("HOBA!!");
        return classCriteria != null ? mongoOperations.find(new Query().addCriteria(classCriteria),
                entityInformation.getJavaType(),
                entityInformation.getCollectionName())
                : super.findAll();
    }
}


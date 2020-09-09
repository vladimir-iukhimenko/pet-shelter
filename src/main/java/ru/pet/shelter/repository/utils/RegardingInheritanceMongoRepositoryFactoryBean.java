package ru.pet.shelter.repository.utils;

import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.repository.support.ReactiveMongoRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class RegardingInheritanceMongoRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends ReactiveMongoRepositoryFactoryBean<T, S, ID> {
    public RegardingInheritanceMongoRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }


    @Override
    protected RepositoryFactorySupport getFactoryInstance(ReactiveMongoOperations operations) {
        return new RegardingInheritanceMongoRepositoryFactory(operations);
    }
}

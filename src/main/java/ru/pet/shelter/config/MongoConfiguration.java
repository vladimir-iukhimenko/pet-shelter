package ru.pet.shelter.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@EnableReactiveMongoRepositories
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "pet_shelter";
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }
}

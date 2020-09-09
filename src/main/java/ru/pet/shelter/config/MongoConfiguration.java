package ru.pet.shelter.config;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.pet.shelter.repository.utils.RegardingInheritanceMongoRepository;
import ru.pet.shelter.repository.utils.RegardingInheritanceMongoRepositoryFactoryBean;

@Configuration
@EnableReactiveMongoRepositories(repositoryBaseClass = RegardingInheritanceMongoRepository.class,
                                repositoryFactoryBeanClass = RegardingInheritanceMongoRepositoryFactoryBean.class,
                                basePackages = {"ru.pet.shelter.repository"})
public class MongoConfiguration extends AbstractReactiveMongoConfiguration {

    private Environment environment;
    private ConnectionString connectionString;

    @Autowired
    public MongoConfiguration(Environment environment) {
        this.environment = environment;
        this.connectionString = new ConnectionString(environment.getRequiredProperty("spring.data.mongodb.uri"));
    }

    /*@Bean
    public MongoClient mongoClient() {
        return MongoClients.create(connectionString);
    }*/

    @Override
    protected String getDatabaseName() {
        return "pet_shelter";
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(new SimpleMongoClientDatabaseFactory(connectionString));
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(new SimpleReactiveMongoDatabaseFactory(connectionString));
    }

}

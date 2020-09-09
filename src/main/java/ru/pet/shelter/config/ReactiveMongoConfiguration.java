package ru.pet.shelter.config;

import com.mongodb.ConnectionString;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import ru.pet.shelter.config.utils.RegardingInheritanceMongoRepository;
import ru.pet.shelter.config.utils.RegardingInheritanceMongoRepositoryFactoryBean;

@Configuration
@EnableReactiveMongoRepositories(repositoryBaseClass = RegardingInheritanceMongoRepository.class,
                                repositoryFactoryBeanClass = RegardingInheritanceMongoRepositoryFactoryBean.class,
                                basePackages = {"ru.pet.shelter.repository"})
public class ReactiveMongoConfiguration extends AbstractReactiveMongoConfiguration {

    private final ConnectionString connectionString;

    @Autowired
    public ReactiveMongoConfiguration(Environment environment) {
        this.connectionString = new ConnectionString(environment.getRequiredProperty("spring.data.mongodb.uri"));
    }

    @Bean
    public MongoClient reactiveMongoClient() {
        return MongoClients.create(connectionString);
    }

    @Override
    protected String getDatabaseName() {
        return "pet_shelter";
    }
}


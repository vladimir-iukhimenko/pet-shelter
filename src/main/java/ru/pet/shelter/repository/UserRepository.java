package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.pet.shelter.config.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
}

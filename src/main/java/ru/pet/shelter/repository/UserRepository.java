package ru.pet.shelter.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.User;
import ru.pet.shelter.dto.UserInfo;

public interface UserRepository extends ReactiveMongoRepository<User, String> {
    Mono<User> findByUserInfo(UserInfo userInfo);
}

package ru.pet.shelter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.pet.shelter.model.User;
import ru.pet.shelter.dto.UserInfo;
import ru.pet.shelter.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> findUserByUserInfo(UserInfo userInfo) {
        return userRepository.findByUserInfo(userInfo);
    }

    public Mono<User> save(User user) { return userRepository.save(user); }
}

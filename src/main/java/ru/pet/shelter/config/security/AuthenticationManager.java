package ru.pet.shelter.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.pet.shelter.dto.UserInfo;
import ru.pet.shelter.model.User;
import ru.pet.shelter.model.helper.Role;
import ru.pet.shelter.service.UserInfoService;
import ru.pet.shelter.service.UserService;

import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private UserInfoService userInfoService;
    private UserService userService;

    @Autowired
    public AuthenticationManager(UserInfoService userInfoService, UserService userService) {
        this.userInfoService = userInfoService;
        this.userService = userService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String accessToken = authentication.getCredentials().toString();
        UserInfo userInfo = userInfoService.getUserInfoFromToken(accessToken);
        return userService.findUserByUserInfo(userInfo)
                .switchIfEmpty(
                        Mono.just(User.builder()
                                .userInfo(userInfo)
                                .roles(List.of(Role.USER))
                                .build())
                                .flatMap(userService::save))
                .doOnNext(SecurityContextHolder.getContext()::setAuthentication)
                .cast(Authentication.class);
    }
}

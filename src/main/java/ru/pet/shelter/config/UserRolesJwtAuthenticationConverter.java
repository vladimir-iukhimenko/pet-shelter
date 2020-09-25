package ru.pet.shelter.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Mono;

public class UserRolesJwtAuthenticationConverter
        implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    private final ReactiveUserDetailsService userDetailsService;

    public UserRolesJwtAuthenticationConverter(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        return userDetailsService.findByUsername(jwt.getClaimAsString("id"))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, "n/a", userDetails.getAuthorities()));
    }
}

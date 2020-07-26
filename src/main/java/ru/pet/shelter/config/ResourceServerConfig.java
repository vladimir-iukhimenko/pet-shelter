package ru.pet.shelter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ResourceServerConfig {

    private static final String[] SECURED_PATTERN = new String[] {};
    private static final String[] NOT_SECURED_PATTERN = new String[] {};

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) throws Exception {
        http.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());

        http.authorizeExchange()
                //.pathMatchers().permitAll()
                .anyExchange().permitAll()
                .and().oauth2Login();
        return http.build();
    }
}

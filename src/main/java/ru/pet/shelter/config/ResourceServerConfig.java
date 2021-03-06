package ru.pet.shelter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class ResourceServerConfig {

    private static final String[] SECURED_PATTERN = new String[] {"/boom"};

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        //http.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());

        http
                .authorizeExchange()
                .pathMatchers(SECURED_PATTERN).authenticated()
                .anyExchange().permitAll()
                .and().oauth2Login().and().oauth2Client();
        return http.build();
    }
}

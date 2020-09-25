package ru.pet.shelter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ResourceServerConfig {

    private final ReactiveUserDetailsService userDetailsService;

    private static final String[] SECURED_PATTERN = new String[] {"/boom"};

    @Autowired
    public ResourceServerConfig(ReactiveUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        //http.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());

        http.csrf()
                .disable()
                .authorizeExchange()
                .pathMatchers(SECURED_PATTERN).authenticated()
                .anyExchange().permitAll()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(userRolesJwtAuthenticationConverter());
        return http.build();
    }

    @Bean
    public UserRolesJwtAuthenticationConverter userRolesJwtAuthenticationConverter() {
        return new UserRolesJwtAuthenticationConverter(userDetailsService);
    }

}

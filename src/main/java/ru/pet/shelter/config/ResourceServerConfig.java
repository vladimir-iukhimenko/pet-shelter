package ru.pet.shelter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import ru.pet.shelter.config.security.AuthenticationManager;
import ru.pet.shelter.config.security.SecurityContextRepository;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class ResourceServerConfig {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    private static final String[] SECURED_PATTERN = new String[] {"/boom"};


    @Autowired
    public ResourceServerConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }


    @Bean
    public SecurityWebFilterChain configure(ServerHttpSecurity http) {
        //http.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint());

        http
                .cors().disable()
                .csrf().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(SECURED_PATTERN).authenticated()
                .anyExchange().permitAll();
        return http.build();
    }
}

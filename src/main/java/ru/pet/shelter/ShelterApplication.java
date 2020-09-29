package ru.pet.shelter;

import com.github.cloudyrock.spring.v5.EnableMongock;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

@SpringBootApplication
@Slf4j
@EnableMongock
public class ShelterApplication {

    public static void main(String[] args) {
        log.info("Application starts...");

        SpringApplication.run(ShelterApplication.class, args);
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info().title("My API").version("1.0"))
                .components(new Components().addSecuritySchemes("OAuth2", securityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("OAuth2"));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("This is Oauth2 scheme")
                .flows(new OAuthFlows().implicit(new OAuthFlow()
                        .authorizationUrl("https://oauth.vk.com/authorize").scopes(new Scopes().addString("offline", "offline"))));
    }

    private SecurityScheme bearerSecurity() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer");
    }

}

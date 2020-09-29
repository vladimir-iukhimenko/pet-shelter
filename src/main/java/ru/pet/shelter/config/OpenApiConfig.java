package ru.pet.shelter.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(info())
                .components(new Components()
                        .addSecuritySchemes("OAuth2 Implicit Flow", oauth2ImplicitSecurityScheme())
                        .addSecuritySchemes("Bearer", bearerHeaderSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("OAuth2 Implicit Flow").addList("Bearer"));
    }

    private Info info() {
        return new Info()
                .title("Pet Shelter REST")
                .version("1.0")
                .description("This is a a backend of Pet Shelter application")
                .contact(new Contact().name("Vladimir Iukhimenko").email("vlad.imenko@yandex.ru"));
    }

    private SecurityScheme oauth2ImplicitSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("This is Oauth2 scheme")
                .flows(new OAuthFlows().implicit(new OAuthFlow()
                        .authorizationUrl("https://oauth.vk.com/authorize").scopes(new Scopes().addString("offline", "offline"))));
    }

    private SecurityScheme bearerHeaderSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer");
    }
}

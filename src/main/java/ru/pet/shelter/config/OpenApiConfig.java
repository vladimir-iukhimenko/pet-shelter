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
                        .addSecuritySchemes("VK OAuth2", vkOauth2ImplicitSecurityScheme())
                        .addSecuritySchemes("VK Bearer", vkBearerHeaderSecurityScheme())
                        .addSecuritySchemes("Github Oauth2", gitHubOauth2AuthorizationSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList("VK OAuth2").addList("VK Bearer"));
    }

    private Info info() {
        return new Info()
                .title("Pet Shelter REST")
                .version("1.0")
                .description("This is a a backend of Pet Shelter application")
                .contact(new Contact().name("Vladimir Iukhimenko").email("vlad.imenko@yandex.ru"));
    }

    private SecurityScheme vkOauth2ImplicitSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("This is Oauth2 scheme")
                .flows(new OAuthFlows().implicit(new OAuthFlow()
                        .authorizationUrl("https://oauth.vk.com/authorize").scopes(new Scopes().addString("offline", "Offline (Necessary for VK Oauth2)"))));
    }

    private SecurityScheme vkBearerHeaderSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer");
    }

    private SecurityScheme gitHubOauth2AuthorizationSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .flows(new OAuthFlows().authorizationCode(new OAuthFlow()
                        .authorizationUrl("https://github.com/login/oauth/authorize")
                        .tokenUrl("https://cors-anywhere.herokuapp.com/https://github.com/login/oauth/access_token")));
    }
}

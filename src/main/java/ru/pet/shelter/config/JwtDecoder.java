package ru.pet.shelter.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JwtDecoder implements ReactiveJwtDecoder {

    @Value("${spring.security.oauth2.client.registration.vk.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.vk.client-secret}")
    private String clientSecret;

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postRequest = new HttpPost("https://api.vk.com/method/secure.checkToken?" + "&client_secret=" + clientSecret + "&client_id=" + clientId + "&v=5.21" + "&token=" + token);
        System.out.println(postRequest.toString());
        try {
            HttpResponse response = client.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            JsonNode json = new ObjectMapper().readTree(reader);
            System.out.println(json);
            if (json.has("error")) throw new IllegalStateException(json.get("error").get("error_msg").asText());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return Mono.empty();
    }
}

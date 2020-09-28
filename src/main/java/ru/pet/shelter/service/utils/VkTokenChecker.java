package ru.pet.shelter.service.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.pet.shelter.dto.UserInfo;
import ru.pet.shelter.dto.VkUserInfo;

import java.io.IOException;
import java.util.Optional;

@Component
public class VkTokenChecker {
    @Value("${spring.security.oauth2.client.registration.vk.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.vk.client-secret}")
    private String clientSecret;


    public Optional<UserInfo> findUserInfoByToken(String token) {
        ObjectMapper mapper = new ObjectMapper();
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost checkToken = new HttpPost("https://api.vk.com/method/secure.checkToken?" + "&client_secret=" + clientSecret + "&client_id=" + clientId + "&v=5.21" + "&token=" + token);
        HttpPost retrieveUserInfo = new HttpPost("https://api.vk.com/method/users.get?" + "&client_secret=" + clientSecret + "&access_token=" + token + "&v=5.89");
        try {

            HttpResponse response = client.execute(checkToken);
            JsonNode json = mapper.readTree(response.getEntity().getContent());

            if (json.has("error")) throw new IllegalStateException(json.get("error").get("error_msg").asText());

            response = client.execute(retrieveUserInfo);
            json = mapper.readTree(response.getEntity().getContent());

            if (json.has("error")) throw new IllegalStateException(json.get("error").get("error_msg").asText());

            json = json.get("response");
            VkUserInfo user = new ObjectMapper().treeToValue(json, VkUserInfo[].class)[0];
            return Optional.of(UserInfo.builder()
                    .userId(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build());

        } catch (IOException e) {

            e.printStackTrace();
            return Optional.empty();

        }
    }

}

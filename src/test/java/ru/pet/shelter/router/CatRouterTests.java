package ru.pet.shelter.router;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.pet.shelter.config.ResourceServerConfig;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.helper.Sex;
import ru.pet.shelter.router.utils.EntityValidator;
import ru.pet.shelter.service.CatService;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {CatRouter.class, EntityValidator.class, ResourceServerConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CatRouterTests {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private CatService catService;

    private WebTestClient webTestClient;

    @BeforeAll
    public void SetUp() {
        webTestClient = WebTestClient
                .bindToApplicationContext(applicationContext)
                .build();
    }

    @Test
    void testFindCatByCorrectId() {
        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Mockito
                .when(catService.findById("100"))
                .thenReturn(Mono.just(cat));

        webTestClient.get().uri("/cat/{id}", 100)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cat.class);

        Mockito.verify(catService, Mockito.times(1)).findById("100");

    }

    @Test
    void testInsertCat() {
        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .status("Живет")
                .shelterId("1")
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Mockito
                .when(catService.save(cat))
                .thenReturn(Mono.just(cat));

        webTestClient.post().uri("/cat")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(cat))
                .exchange()
                .expectStatus().isCreated();

        Mockito.verify(catService, Mockito.times(1)).save(cat);
    }
}

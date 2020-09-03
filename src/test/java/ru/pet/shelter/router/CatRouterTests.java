package ru.pet.shelter.router;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.repository.CatRepository;

@SpringBootTest
public class CatRouterTests {

    @Autowired
    private CatRouter catRouter;

    @MockBean
    private CatRepository catRepository;

    public void catGetterTest() {
        WebTestClient client = WebTestClient.bindToRouterFunction(catRouter.catRoutes()).build();

        Cat cat = new Cat();


    }
}

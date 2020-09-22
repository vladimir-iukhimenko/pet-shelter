package ru.pet.shelter.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.pet.shelter.model.Cat;
import ru.pet.shelter.model.helper.Sex;

import java.time.LocalDate;

@DataMongoTest
public class CatRepositoryTests {

    @Autowired
    private PetRepository petRepository;

    @Test
    void testSaveCatEntity() {
        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Mono<Cat> result = petRepository.saveCat(cat);

        StepVerifier.create(result).expectNext(cat).verifyComplete();
        //assertThat(petRepository.findAllCat().blockFirst()).isEqualToComparingFieldByField(cat);

    }
}

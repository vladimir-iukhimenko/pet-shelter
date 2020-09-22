package ru.pet.shelter.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
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
    void testFindCatByCorrectId() {

        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Mono<Cat> result = petRepository.saveCat(cat)
                .then(petRepository.findCatById("100"));

        StepVerifier.create(result).expectNext(cat).verifyComplete();

    }

    @Test
    void testFindCatByIncorrectId() {

        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Mono<Cat> result = petRepository.saveCat(cat)
                .then(petRepository.findCatById("200"));

        StepVerifier.create(result).expectNextCount(0).verifyComplete();

    }

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

    }

    @Test
    void testUpdateCatEntity() {

        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        petRepository.saveCat(cat);
        Cat updatedCat = Cat.builder()
                .id("100")
                .name("Барсик")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 3, 15))
                .breed("Шотландская вислоухая")
                .build();
        Mono<Cat> result = petRepository.updateCat(updatedCat);
        StepVerifier.create(result).expectNext(updatedCat).verifyComplete();
    }

    @Test
    void testRemoveCatEntity() {

        Cat cat = Cat.builder()
                .id("100")
                .name("Марсель")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 2, 15))
                .breed("Сиамская кошка")
                .build();

        Cat catForDeletion = Cat.builder()
                .id("200")
                .name("Барсик")
                .sex(Sex.M)
                .born(LocalDate.of(2019, 3, 15))
                .breed("Шотландская вислоухая")
                .build();

        Cat oneMoreCat = Cat.builder()
                .id("300")
                .name("Мурзик")
                .sex(Sex.M)
                .born(LocalDate.of(2018, 7, 1))
                .breed("Беспородная")
                .build();
        Flux<Cat> result = petRepository.saveCat(cat)
                .then(petRepository.saveCat(catForDeletion))
                .then(petRepository.saveCat(oneMoreCat))
                .then(petRepository.removeCatById("200"))
                .thenMany(petRepository.findAllCat());
        StepVerifier.create(result).expectNext(cat, oneMoreCat).verifyComplete();
    }
}

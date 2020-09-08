package ru.pet.shelter.db.migrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.pet.shelter.model.*;
import ru.pet.shelter.model.helper.Sex;

import java.time.LocalDate;

@ChangeLog(order = "20200904")
public class DatabaseInitializeChangeLog {

    @ChangeSet(id = "initializeDocumentCollections", order = "001", author = "Vladimir Iukhimenko")
    public void initializeDescriptionDocument(MongockTemplate mongockTemplate) {
        mongockTemplate.createCollection(Shelter.class);
        mongockTemplate.createCollection(Request.class);
    }

    @ChangeSet(id = "insertInitialShelterData", order = "002", author = "Vladimir Iukhimenko")
    public void inputInitialShelterData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(Shelter.builder()
                .type("Федеральный")
                .city("Москва")
                .name("Добрый приют")
                .build(), "shelter");
        mongockTemplate.save(Shelter.builder()
                .type("Частный")
                .city("Санкт-Петербург")
                .name("Надежда")
                .build(), "shelter");
        mongockTemplate.save(Shelter.builder()
                .type("Муниципальный")
                .city("Калуга")
                .name("Забота")
                .build(), "shelter");
    }

    @ChangeSet(id = "insertInitialPetData", order = "003", author = "Vladimir Iukhimenko")
    public void inputInitialPetData(MongockTemplate mongockTemplate) {
        Shelter shelterCare = mongockTemplate.query(Shelter.class).matching(new Query(Criteria.where("name").is("Забота"))).oneValue();
        Shelter shelterHope = mongockTemplate.query(Shelter.class).matching(new Query(Criteria.where("name").is("Надежда"))).oneValue();
        Shelter shelterKind = mongockTemplate.query(Shelter.class).matching(new Query(Criteria.where("name").is("Добрый приют"))).oneValue();
        mongockTemplate.save(Cat.builder()
                .name("Барсик")
                .born(LocalDate.now())
                .sex(Sex.M)
                .isSterialized(true)
                .status("Проживает")
                .shelterByShelterId(shelterCare)
                .length(70)
                .weight(4500)
                .breed("Мейн-кун")
                .passport(Passport.builder().number(6000002500L).build())
                .build(), "pet");
        mongockTemplate.save(Cat.builder()
                .name("Мурзик")
                .born(LocalDate.now())
                .sex(Sex.M)
                .isSterialized(true)
                .status("Проживает")
                .shelterByShelterId(shelterHope)
                .length(50)
                .weight(3200)
                .breed("Сиамская")
                .passport(Passport.builder().number(61450004301L).build())
                .build(), "pet");
        mongockTemplate.save(Dog.builder()
                .name("Кира")
                .born(LocalDate.now())
                .sex(Sex.F)
                .isSterialized(true)
                .status("Проживает")
                .shelterByShelterId(shelterKind)
                .length(80)
                .weight(8900)
                .breed("Бигль")
                .passport(Passport.builder().number(260012502910L).build())
                .build(), "pet");
    }

}

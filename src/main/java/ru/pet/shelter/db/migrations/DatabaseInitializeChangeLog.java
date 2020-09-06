package ru.pet.shelter.db.migrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import org.bson.BsonArray;
import org.bson.BsonValue;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.pet.shelter.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@ChangeLog(order = "20200904")
public class DatabaseInitializeChangeLog {

    @ChangeSet(id = "initializeRefCityData", order = "000", author = "Vladimir Iukhimenko")
    public void initializeRefCitiesData(MongockTemplate mongockTemplate) throws IOException {
        mongockTemplate.createCollection(RefCity.class);
        var stringCities = Files.readString(Paths.get("src\\main\\resources\\cities.json"));
        var data = BsonArray.parse(stringCities);
        for (BsonValue value: data) {
            var region = value.asDocument().get("region").asString().getValue();
            var city = value.asDocument().get("city").asString().getValue();
            mongockTemplate.save(RefCity.builder().region(region).city(city).build(), "ref_city");
        }
    }

    @ChangeSet(id = "initializeDocumentCollections", order = "001", author = "Vladimir Iukhimenko")
    public void initializeDescriptionDocument(MongockTemplate mongockTemplate) {
        mongockTemplate.createCollection(Cat.class);
        mongockTemplate.createCollection(Chip.class);
        mongockTemplate.createCollection(Description.class);
        mongockTemplate.createCollection(Dog.class);
        mongockTemplate.createCollection(Passport.class);
        mongockTemplate.createCollection(PassportVaccination.class);
        mongockTemplate.createCollection(Photo.class);
        mongockTemplate.createCollection(RefCatBreed.class);
        mongockTemplate.createCollection(RefDogBreed.class);
        mongockTemplate.createCollection(RefFur.class);
        mongockTemplate.createCollection(RefFurColor.class);
        mongockTemplate.createCollection(RefShelter.class);
        mongockTemplate.createCollection(RefVaccination.class);
        mongockTemplate.createCollection(Request.class);
    }

    @ChangeSet(id = "insertInitialChipData", order = "002", author = "Vladimir Iukhimenko")
    public void inputInitialChipData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(Chip.builder().chipNumber(102659000L).chipDate(LocalDate.now()).build(),"chip");
        mongockTemplate.save(Chip.builder().chipNumber(102659001L).chipDate(LocalDate.now()).build(), "chip");
        mongockTemplate.save(Chip.builder().chipNumber(102659002L).chipDate(LocalDate.now()).build(), "chip");
    }

    @ChangeSet(id = "insertInitialCatBreedData", order = "003", author = "Vladimir Iukhimenko")
    public void inputInitialRefCatBreedData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(RefCatBreed.builder().breedName("Мейн-кун").build(), "ref_cat_breed");
        mongockTemplate.save(RefCatBreed.builder().breedName("Бенгальская").build(), "ref_cat_breed");
        mongockTemplate.save(RefCatBreed.builder().breedName("Сиамская").build(), "ref_cat_breed");
    }

    @ChangeSet(id = "insertInitialDogBreedData", order = "004", author = "Vladimir Iukhimenko")
    public void inputInitialRefDogBreedData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(RefDogBreed.builder().breedName("Бигль").build(), "ref_dog_breed");
        mongockTemplate.save(RefDogBreed.builder().breedName("Бульдог").build(), "ref_dog_breed");
        mongockTemplate.save(RefDogBreed.builder().breedName("Мопс").build(), "ref_dog_breed");
    }

    @ChangeSet(id = "insertInitialFurData", order = "005", author = "Vladimir Iukhimenko")
    public void inputInitialRefFurData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(RefFur.builder().furName("Короткошёрстный").build(), "ref_fur");
        mongockTemplate.save(RefFur.builder().furName("Полудлинношерстный").build(), "ref_fur");
        mongockTemplate.save(RefFur.builder().furName("Длинношерстный").build(), "ref_fur");
    }

    @ChangeSet(id = "insertInitialFurColorData", order = "006", author = "Vladimir Iukhimenko")
    public void inputInitialRefFurColorData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(RefFurColor.builder().furColorName("Чёрный").build(), "ref_fur_color");
        mongockTemplate.save(RefFurColor.builder().furColorName("Шоколадный").build(), "ref_fur_color");
        mongockTemplate.save(RefFurColor.builder().furColorName("Белый").build(), "ref_fur_color");
    }

    @ChangeSet(id="insertInitialVaccinationData", order = "007", author = "Vladimir Iukhimenko")
    public void inputInitialRefVaccinationData(MongockTemplate mongockTemplate) {
        mongockTemplate.save(RefVaccination.builder().vaccineName("Нобивак").build(), "ref_vaccination");
        mongockTemplate.save(RefVaccination.builder().vaccineName("Фелиген").build(), "ref_vaccination");
        mongockTemplate.save(RefVaccination.builder().vaccineName("Мультифел-4").build(), "ref_vaccination");
    }

    @ChangeSet(id="insertInitialShelterData", order = "008", author = "Vladimir Iukhimenko")
    public void inputInitialRefShelter(MongockTemplate mongockTemplate) {
        RefCity moscowCity = mongockTemplate.query(RefCity.class).matching(new Query(Criteria.where("city").is("Москва"))).oneValue();
        RefCity sochiCity = mongockTemplate.query(RefCity.class).matching(new Query(Criteria.where("city").is("Сочи"))).oneValue();
        RefCity kazanCity = mongockTemplate.query(RefCity.class).matching(new Query(Criteria.where("city").is("Казань"))).oneValue();
        mongockTemplate.save(RefShelter.builder().city(moscowCity).type("Государственный").build(), "ref_shelter");
        mongockTemplate.save(RefShelter.builder().city(sochiCity).type("Муниципальный").build(), "ref_shelter");
        mongockTemplate.save(RefShelter.builder().city(kazanCity).type("Частный").build(), "ref_shelter");
    }

    public void inputInitialCatData() {
    }
}

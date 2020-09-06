package ru.pet.shelter.db.migrations;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.github.cloudyrock.mongock.driver.mongodb.springdata.v3.decorator.impl.MongockTemplate;
import reactor.core.publisher.Flux;
import ru.pet.shelter.model.*;
import ru.pet.shelter.service.CatService;

@ChangeLog(order = "20200904")
public class DatabaseInitializeChangeLog {

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

    public void inputInitialCatData(CatService catService) {
    }
}

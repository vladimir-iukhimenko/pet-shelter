package ru.pet.shelter.model;

import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@SuperBuilder
public abstract class Pet {

    @NotNull
    String name;
    LocalDate born;
    Sex sex;
    Boolean isSterialized;
    Photo avatarByPhotoId;
    String fur;
    String furColor;
    //private Integer curatorByCuratorId; todo:user entity
    @NotNull
    String status;
    LocalDate appearanceDate;
    String features;

    @DBRef(db = "pet_shelter")
    @NotNull
    Shelter shelterByShelterId;
}

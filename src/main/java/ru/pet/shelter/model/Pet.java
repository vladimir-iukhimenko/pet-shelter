package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public abstract class Pet {

    @NotNull
    String name;
    LocalDate born;
    Sex sex;
    Boolean isSterialized;
    Photo avatarByPhotoId;
    //private Integer curatorByCuratorId; todo:user entity
    @NotNull
    String status;
    LocalDate appearanceDate;
    String features;
    @NotNull
    RefShelter shelterByRefShelterId;
}

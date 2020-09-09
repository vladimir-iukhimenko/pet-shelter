package ru.pet.shelter.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@Document(collection = "pet")
public abstract class Pet {

    @Id
    String id;
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

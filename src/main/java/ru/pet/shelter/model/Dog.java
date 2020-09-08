package ru.pet.shelter.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Document(collection = "pet")
@SuperBuilder
public class Dog extends Pet {

    @Id
    private ObjectId id;
    private Integer length;
    private Integer height;
    private Integer weight;
    @NotNull
    private String breed;
    private Passport passport;
    //private Integer curatorByCuratorId; todo:user entity
    private Chip chipByChipid;
    private List<Description> description;

}

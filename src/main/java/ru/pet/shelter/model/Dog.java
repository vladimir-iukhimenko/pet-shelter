package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Document(collection = "dog")
@AllArgsConstructor
@NoArgsConstructor
public class Dog extends Pet {

    @Id
    private String dogId;
    private Integer length;
    private Integer height;
    private Integer weight;
    @NotNull
    private RefDogBreed breedByBreedId;
    private Passport passportByPassportId;
    private RefFur furByRefFurId;
    private RefFurColor furColorByRefFurColorId;
    //private Integer curatorByCuratorId; todo:user entity
    private Chip chipByChipid;
    private Set<Description> description;

}

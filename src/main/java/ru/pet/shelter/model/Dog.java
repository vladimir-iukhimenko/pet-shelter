package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
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

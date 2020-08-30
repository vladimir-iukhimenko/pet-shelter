package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Document(collection = "cat")
@AllArgsConstructor
@NoArgsConstructor
public class Cat extends Pet{

    @Id
    private String catId;
    private Integer length;
    private Integer weight;
    @NotNull
    private RefCatBreed breedByBreedId;
    private Passport passportByPassportId;
    private RefFur furByRefFurId;
    private RefFurColor furColorByRefFurColorId;
    //private Integer curatorByCuratorId; todo:user entity
    private Chip chipByChipid;
    private Set<Description> description;
}

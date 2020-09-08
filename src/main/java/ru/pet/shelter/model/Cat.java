package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@Document(collection = "pet")
@SuperBuilder
public class Cat extends Pet {

    @Id
    private ObjectId id;
    private Integer length;
    private Integer weight;
    @NotNull
    private String breed;
    private Passport passport;
    //private Integer curatorByCuratorId; todo:user entity
    private Chip chip;
    private Set<Description> description;
}

package ru.pet.shelter.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.pet.shelter.model.helper.Sex;
import ru.pet.shelter.model.view.PetView;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
    Photo avatar;
    String fur;
    String furColor;
    //private Integer curatorByCuratorId; todo:user entity
    @NotNull
    String status;
    LocalDate appearanceDate;
    String features;
    @NotNull
    @JsonView(PetView.INTERNAL.class)
    String shelterId;
    @Transient
    Shelter shelter;
    List<Photo> photos;
    List<Description> description;
}

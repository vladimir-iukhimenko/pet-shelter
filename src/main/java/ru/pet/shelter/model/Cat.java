package ru.pet.shelter.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Document(collection = "pet")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@TypeAlias("cat")
public class Cat extends Pet {

    private Integer length;
    private Integer weight;
    @NotNull
    private String breed;
    private Passport passport;
    //private Integer curatorByCuratorId; todo:user entity
    private Chip chip;
    private Set<Description> description;
}

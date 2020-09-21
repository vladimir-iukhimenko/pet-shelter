package ru.pet.shelter.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Document(collection = "pet")
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@TypeAlias("dog")
public class Dog extends Pet {

    private Integer length;
    private Integer height;
    private Integer weight;
    @NotNull
    @Schema(description = "Порода")
    private String breed;
    @Valid
    private Passport passport;
    //private Integer curatorByCuratorId; todo:user entity
    @Valid
    private Chip chip;


}

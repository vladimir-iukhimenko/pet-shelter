package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Dog {

    @Id
    private Long dogId;
    private Integer length;
    private Integer height;
    private Integer weight;
    @NotNull
    private RefDogBreed breedByBreedId;

}

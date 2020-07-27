package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Cat {

    @Id
    private Long catId;
    private Integer length;
    private Integer weight;
    @NotNull
    private RefCatBreed breedByBreedId;
}

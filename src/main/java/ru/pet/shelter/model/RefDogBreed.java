package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RefDogBreed {

    @Id
    private Long breedId;
    @NotNull
    private String breedName;
}

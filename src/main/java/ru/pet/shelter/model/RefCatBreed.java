package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RefCatBreed {

    @Id
    private String breedId;
    @NotNull
    private String breedName;
}

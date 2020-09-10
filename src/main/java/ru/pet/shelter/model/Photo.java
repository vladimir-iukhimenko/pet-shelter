package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Photo {
    @NotNull
    private String photoName;
    @NotNull
    private String photoPath;

}

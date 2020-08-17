package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class Photo {

    @Id
    private String photoId;
    @NotNull
    private Pet petByPetId;
    @NotNull
    private String photoName;

}

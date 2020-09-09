package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class Photo {

    @Id
    private String id;
    @NotNull
    private Pet petByPetId;
    @NotNull
    private String photoName;

}

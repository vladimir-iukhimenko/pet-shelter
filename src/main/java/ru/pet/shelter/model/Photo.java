package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "photo")
@AllArgsConstructor
@NoArgsConstructor
public class Photo {

    @Id
    private String photoId;
    @NotNull
    private Pet petByPetId;
    @NotNull
    private String photoName;

}

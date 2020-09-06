package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "ref_dog_breed")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefDogBreed {

    @Id
    private String breedId;
    @NotNull
    private String breedName;
}

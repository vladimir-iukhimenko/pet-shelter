package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "ref_shelter")
@AllArgsConstructor
@NoArgsConstructor
public class RefShelter {

    @Id
    private String id;
    @NotNull
    @Max(value = 32)
    private String type;
    @NotNull
    @Max(value = 32)
    private String city;
}

package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "shelter")
@Builder
public class Shelter {

    @Id
    private String id;
    @NotNull
    @Max(value = 32)
    private String type;
    @NotNull
    private String city;
    @NotNull
    private String name;
}

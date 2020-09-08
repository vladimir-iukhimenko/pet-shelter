package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class RefVaccination {

    @Id
    private ObjectId id;
    @NotNull
    @Max(value = 50)
    private String vaccineName;

}

package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class Passport {

    @Id
    private ObjectId id;
    @NotNull
    private Long number;
    private Photo photoByPhotoId;
    private Set<PassportVaccination> passportVaccinations;

}

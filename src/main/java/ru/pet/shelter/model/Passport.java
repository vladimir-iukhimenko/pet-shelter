package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Document(collection = "passport")
@AllArgsConstructor
@NoArgsConstructor
public class Passport {

    @Id
    private String passportId;
    @NotNull
    private Long number;
    private Photo photoByPhotoId;
    private Set<PassportVaccination> passportVaccinations;

}

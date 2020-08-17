package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
public class Passport {

    @Id
    private String passportId;
    @NotNull
    private Long number;
    private Photo photoByPhotoId;
    private Set<PassportVaccination> passportVaccinations;

}

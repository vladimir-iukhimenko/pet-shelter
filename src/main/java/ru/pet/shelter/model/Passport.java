package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
public class Passport {

    @NotNull
    private Long number;
    @Valid
    private Photo photo;
    @Valid
    private Set<PassportVaccination> passportVaccinations;

}

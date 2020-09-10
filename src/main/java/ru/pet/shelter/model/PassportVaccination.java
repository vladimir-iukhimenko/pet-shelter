package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class PassportVaccination {

    @NotNull
    private String vaccineName;
    @NotNull
    private LocalDate validityStart;
    @NotNull
    private LocalDate validityEnd;


}

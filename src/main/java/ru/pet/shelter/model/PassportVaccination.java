package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PassportVaccination {
    @Id
    private Long id;
    @NotNull
    private Passport passportByPassportId;
    @NotNull
    private RefVaccination vaccineByvaccineId;
    @NotNull
    private LocalDate validityStart;
    @NotNull
    private LocalDate validityEnd;


}

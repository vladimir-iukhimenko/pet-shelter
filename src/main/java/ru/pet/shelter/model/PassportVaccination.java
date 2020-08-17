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
    private String id;
    @NotNull
    private RefVaccination vaccineByVaccineId;
    @NotNull
    private LocalDate validityStart;
    @NotNull
    private LocalDate validityEnd;


}

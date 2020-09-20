package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class PassportVaccination {

    @Id
    private String id;
    @NotNull
    private String vaccineName;
    @NotNull
    private LocalDate validityStart;
    @NotNull
    private LocalDate validityEnd;

}

package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "passport_vaccination")
@AllArgsConstructor
@NoArgsConstructor
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

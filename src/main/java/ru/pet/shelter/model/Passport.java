package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Passport {

    @Id
    @NotNull
    private String number;
    @Valid
    private Photo photo;
    @Valid
    private Set<PassportVaccination> passportVaccinations;

}

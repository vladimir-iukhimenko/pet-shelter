package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class Passport {

    @Id
    private String id;
    @NotNull
    private String number;
    @Valid
    private Photo photo;
    @Valid
    private List<PassportVaccination> passportVaccinations;

}

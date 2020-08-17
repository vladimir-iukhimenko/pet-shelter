package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RefVaccination {

    @Id
    private String  id;
    @NotNull
    @Max(value = 50)
    private String vaccineName;

}

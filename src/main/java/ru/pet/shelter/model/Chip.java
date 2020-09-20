package ru.pet.shelter.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class Chip {

    @Id
    String id;
    @NotNull
    private String chipNumber;
    @NotNull
    private LocalDate chipDate;

}

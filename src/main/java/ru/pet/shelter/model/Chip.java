package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Chip {

    @Id
    private Long chipId;
    @NotNull
    private LocalDate chipDate;
    @NotNull
    private Long chipNumber;
}

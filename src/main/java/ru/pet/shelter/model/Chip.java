package ru.pet.shelter.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chip {

    @Id
    @NotNull
    private String chipNumber;

    @NotNull
    private LocalDate chipDate;

}

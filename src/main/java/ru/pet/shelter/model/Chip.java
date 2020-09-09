package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class Chip {

    @Id
    private String id;
    @NotNull
    private LocalDate chipDate;
    @NotNull
    private Long chipNumber;
}

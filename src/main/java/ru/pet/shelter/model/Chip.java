package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "chip")
@ToString
@AllArgsConstructor
public class Chip {

    @Id
    private String chipId;
    @NotNull
    private LocalDate chipDate;
    @NotNull
    private Long chipNumber;
}

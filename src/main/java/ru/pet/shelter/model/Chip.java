package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "chip")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Chip {

    @Id
    private String chipId;
    @NotNull
    private LocalDate chipDate;
    @NotNull
    private Long chipNumber;
}

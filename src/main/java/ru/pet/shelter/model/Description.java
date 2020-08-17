package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Description {

    @Id
    private String id;
    @NotNull
    private String descriptionText;
    @NotNull
    private LocalDate timeAdded;

}

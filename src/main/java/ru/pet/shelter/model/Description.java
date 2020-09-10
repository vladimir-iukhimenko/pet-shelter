package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class Description {

    @NotNull
    private String descriptionText;
    @NotNull
    private LocalDate timeAdded;

}

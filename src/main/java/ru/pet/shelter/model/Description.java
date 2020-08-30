package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "description")
@AllArgsConstructor
@NoArgsConstructor
public class Description {

    @Id
    private String id;
    @NotNull
    private String descriptionText;
    @NotNull
    private LocalDate timeAdded;

}

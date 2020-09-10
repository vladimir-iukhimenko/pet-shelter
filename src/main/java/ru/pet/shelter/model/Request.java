package ru.pet.shelter.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.pet.shelter.model.view.PetView;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "request")
@Builder
public class Request {

    @Id
    private String id;
    @NotNull
    private LocalDate requestDate;
    @NotNull
    @JsonView(PetView.INTERNAL.class)
    private String petId;
    @Transient
    private Pet pet;
    @NotNull
    private Long phoneNumber;
    private String comment;

}

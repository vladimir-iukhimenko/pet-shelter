package ru.pet.shelter.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Pet petByPetId;
    @NotNull
    private Long phoneNumber;
    private String comment;

}

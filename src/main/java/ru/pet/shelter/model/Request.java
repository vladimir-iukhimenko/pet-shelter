package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Document(collection = "request")
@AllArgsConstructor
@NoArgsConstructor
public class Request {

    @Id
    private String requestId;
    @NotNull
    private LocalDate requestDate;
    @NotNull
    private Pet petByPetId;
    @NotNull
    private Long phoneNumber;
    private String comment;

}

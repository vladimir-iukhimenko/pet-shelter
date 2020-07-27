package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Request {

    @Id
    private Long requestId;
    @NotNull
    private LocalDate requestDate;
    @NotNull
    private Pet petByPetId;
    @NotNull
    private Long phoneNumber;
    private String comment;

}

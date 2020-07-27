package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RefFur {

    @Id
    private Long furId;
    @NotNull
    private String furName;
}

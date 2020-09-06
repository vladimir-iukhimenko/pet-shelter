package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Document(collection = "ref_vaccination")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefVaccination {

    @Id
    private String id;
    @NotNull
    @Max(value = 50)
    private String vaccineName;

}

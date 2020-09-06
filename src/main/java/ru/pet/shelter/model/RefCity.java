package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "ref_city")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefCity {

    @Id
    private String id;
    @NotNull
    private String region;
    @NotNull
    private String city;
}

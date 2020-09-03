package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "ref_fur_color")
@AllArgsConstructor
@NoArgsConstructor
public class RefFurColor {

    @Id
    private String furColorId;
    @NotNull
    private String furColorName;

}

package ru.pet.shelter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import ru.pet.shelter.model.helper.Sex;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Pet {

    @Id
    private Long petId;
    @NotNull
    private String name;
    private LocalDate born;
    private Passport passportByPassportId;
    private RefFur furByRefFurId;
    private RefFurColor furColorByRefFurColorId;
    @NotNull
    private Sex sex;
    @NotNull
    private Boolean isSterialized;
    private Photo avatarByPhotoId;
    //private Integer curatorByCuratorId; todo:user entity
    @NotNull
    private String status;
    private LocalDate appearanceDate;
    private String features;
    private Chip chipByChipid;
    @NotNull
    private RefShelter shelterByRefShelterId;
}

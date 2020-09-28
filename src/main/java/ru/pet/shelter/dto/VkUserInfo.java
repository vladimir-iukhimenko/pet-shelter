package ru.pet.shelter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VkUserInfo {
    private Integer id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("is_closed")
    private Boolean isClosed;
    @JsonProperty("can_access_closed")
    private Boolean canAccessClosed;

}

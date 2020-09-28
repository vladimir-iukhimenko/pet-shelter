package ru.pet.shelter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInfo {
    private final Integer userId;
    private final String firstName;
    private final String lastName;
}

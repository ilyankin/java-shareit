package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@Builder
public class UserDto {
    private Long id;
    @NotBlank
    @Length(min = 1, max = 128)
    private String name;
    @Email
    @NotBlank
    private String email;
}

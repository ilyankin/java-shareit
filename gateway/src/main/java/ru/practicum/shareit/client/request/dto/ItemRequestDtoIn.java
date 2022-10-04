package ru.practicum.shareit.client.request.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ItemRequestDtoIn {
    @NotBlank
    private String description;
}

package ru.practicum.shareit.client.comment.dto;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentDtoIn {
    @NotBlank
    private String text;
}

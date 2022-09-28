package ru.practicum.shareit.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CommentDtoOut {
    private Long id;
    private String text;
    private String itemName;
    private String authorName;
    private LocalDateTime created;
}

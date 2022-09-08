package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDtoOut;
import ru.practicum.shareit.comment.dto.CommentDtoOut;

import java.util.List;


@Data
@NoArgsConstructor
public class ItemDtoOut {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private BookingItemDtoOut lastBooking;
    private BookingItemDtoOut nextBooking;
    private List<CommentDtoOut> comments;
}

package ru.practicum.shareit.requests;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.time.LocalDateTime;

@Component
public final class ItemRequestMapper {
    public ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requesterId(itemRequest.getRequester().getId())
                .created(itemRequest.getCreated())
                .build();

    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        final ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(itemRequestDto.getCreated() == null ? LocalDateTime.now() : itemRequestDto.getCreated());
        return itemRequest;
    }
}

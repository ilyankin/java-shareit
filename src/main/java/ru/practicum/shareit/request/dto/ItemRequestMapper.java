package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {
    public static ItemRequestDtoOut toItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDtoOut itemRequestDtoOut = new ItemRequestDtoOut();
        itemRequestDtoOut.setId(itemRequest.getId());
        itemRequestDtoOut.setDescription(itemRequest.getDescription());
        itemRequestDtoOut.setCreated(itemRequest.getCreated());

        List<Item> items = itemRequest.getItems();
        if (items != null) {
            itemRequestDtoOut.setItems(ItemMapper.toItemDtos(itemRequest.getItems()));
        }

        return itemRequestDtoOut;
    }

    public static List<ItemRequestDtoOut> toItemRequestDtos(List<ItemRequest> itemRequest) {
        return itemRequest.stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    public static ItemRequest toItemRequest(ItemRequestDtoIn itemRequestDto) {
        final ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        return itemRequest;
    }
}

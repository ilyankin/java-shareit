package ru.practicum.shareit.item;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public final class ItemMapper {
    public ItemDtoOut toItemDto(Item item) {
        return ItemDtoOut.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    public List<ItemDtoOut> toItemDtos(List<Item> items) {
        return items.stream().map(this::toItemDto).collect(Collectors.toList());
    }

    public Item toItem(ItemDtoIn itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}

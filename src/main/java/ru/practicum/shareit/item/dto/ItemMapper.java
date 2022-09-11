package ru.practicum.shareit.item.dto;


import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

public final class ItemMapper {
    public static ItemDtoOut toItemDto(Item item) {
        final ItemDtoOut itemDto = new ItemDtoOut();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static List<ItemDtoOut> toItemDtos(List<Item> items) {
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    public static Item toItem(ItemDtoIn itemDto) {
        final Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }
}

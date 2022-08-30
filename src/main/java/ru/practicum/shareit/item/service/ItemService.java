package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDtoIn;
import ru.practicum.shareit.item.dto.ItemDtoOut;

import java.util.List;


public interface ItemService {
    ItemDtoOut getItemById(Long itemId, Long userId);

    ItemDtoOut saveItem(ItemDtoIn itemDtoIn, Long userId);

    ItemDtoOut updateItem(ItemDtoIn itemDtoIn, Long itemId, Long userId);

    List<ItemDtoOut> getAllItems(Long userId);

    List<ItemDtoOut> searchItem(String text);
}

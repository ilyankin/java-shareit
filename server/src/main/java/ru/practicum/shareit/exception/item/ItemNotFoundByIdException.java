package ru.practicum.shareit.exception.item;

import ru.practicum.shareit.exception.ResourceNotFoundException;

public class ItemNotFoundByIdException extends ResourceNotFoundException {
    public ItemNotFoundByIdException(Long itemId) {
        super("Item", "id", itemId);
    }
}

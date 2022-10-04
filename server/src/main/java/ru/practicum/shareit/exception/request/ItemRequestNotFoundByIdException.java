package ru.practicum.shareit.exception.request;

import ru.practicum.shareit.exception.ResourceNotFoundException;

public class ItemRequestNotFoundByIdException extends ResourceNotFoundException {
    public <T> ItemRequestNotFoundByIdException(T valueName) {
        super("ItemRequest", "id", valueName);
    }
}

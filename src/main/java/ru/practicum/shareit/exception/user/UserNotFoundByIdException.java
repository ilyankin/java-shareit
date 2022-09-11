package ru.practicum.shareit.exception.user;

import ru.practicum.shareit.exception.ResourceNotFoundException;

public class UserNotFoundByIdException extends ResourceNotFoundException {
    public UserNotFoundByIdException(Long userId) {
        super("User", "id", userId);
    }
}

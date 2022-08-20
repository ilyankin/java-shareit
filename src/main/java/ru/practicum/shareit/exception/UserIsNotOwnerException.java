package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserIsNotOwnerException extends RuntimeException {
    public UserIsNotOwnerException(Long userId, Long itemId) {
        super(String.format("User with this {id=%s} is not the owner of item with {id=%s}.", userId, itemId));
    }
}

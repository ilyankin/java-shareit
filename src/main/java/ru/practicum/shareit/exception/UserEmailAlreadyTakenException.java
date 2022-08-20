package ru.practicum.shareit.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailAlreadyTakenException extends EntityAlreadyTakenException {
    public <T> UserEmailAlreadyTakenException(T valueName) {
        super("User", "email", valueName);
    }
}

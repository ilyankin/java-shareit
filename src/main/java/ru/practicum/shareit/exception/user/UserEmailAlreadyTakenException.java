package ru.practicum.shareit.exception.user;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.shareit.exception.EntityAlreadyTakenException;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailAlreadyTakenException extends EntityAlreadyTakenException {
    public <T> UserEmailAlreadyTakenException(T valueName) {
        super("User", "email", valueName);
    }
}

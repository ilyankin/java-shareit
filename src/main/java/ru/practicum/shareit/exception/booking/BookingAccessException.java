package ru.practicum.shareit.exception.booking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BookingAccessException extends BookingException {
    public BookingAccessException(String message) {
        super(message);
    }
}

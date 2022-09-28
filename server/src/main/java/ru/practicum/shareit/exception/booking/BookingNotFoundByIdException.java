package ru.practicum.shareit.exception.booking;

import ru.practicum.shareit.exception.ResourceNotFoundException;

public class BookingNotFoundByIdException extends ResourceNotFoundException {
    public BookingNotFoundByIdException(Long bookingId) {
        super("Booking", "id", bookingId);
    }
}

package ru.practicum.shareit.exception.booking;

public class UnknownBookingStateException extends BookingException {
    public UnknownBookingStateException(String bookingState) {
        super("Unknown state: " + bookingState);
    }
}

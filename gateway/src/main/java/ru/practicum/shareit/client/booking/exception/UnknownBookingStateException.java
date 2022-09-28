package ru.practicum.shareit.client.booking.exception;


public class UnknownBookingStateException extends BookingException {
    public UnknownBookingStateException(String bookingState) {
        super("Unknown state: " + bookingState);
    }
}

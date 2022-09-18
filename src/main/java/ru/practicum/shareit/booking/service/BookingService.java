package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.List;

public interface BookingService {
    BookingDtoOut saveBooking(BookingDtoIn bookingDtoIn, Long bookerId);

    BookingDtoOut getBooking(Long userId, Long bookingId);

    List<BookingDtoOut> getAllBookingsByBookerId(Long bookerId, String state, Integer from, Integer size);

    List<BookingDtoOut> getAllBookingsByItemsOwnerId(Long itemsOwnerId, String state, Integer from, Integer size);

    BookingDtoOut updateBookingStatus(Long bookingId, Long userId, boolean approved);
}

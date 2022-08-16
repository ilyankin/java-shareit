package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    public BookingDtoOut toDto(Booking booking) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(booking.getBooker())
                .item(booking.getItem())
                .status(booking.getStatus())
                .build();
    }

    public Booking toBooking(BookingDtoIn bookingDto) {
        var booking = new Booking();
        var start = bookingDto.getStart();
        var end = bookingDto.getEnd();
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Дата начала букинга после даты конца");
        }
        booking.setStart(start);
        booking.setEnd(start);
        // booking.setItem(); // TODO: add mechanism for getting item objects
        return booking;
    }
}

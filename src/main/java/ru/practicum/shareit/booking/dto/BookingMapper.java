package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.item.ItemNotFoundByIdException;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

public final class BookingMapper {
    public static BookingDtoOut toBookingDto(Booking booking) {
        final BookingDtoOut bookingDto = new BookingDtoOut();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(booking.getItem());
        bookingDto.setBooker(booking.getBooker());
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public static BookingItemDtoOut toBookingItemDtoOut(Booking booking) {
        if (booking == null) return null;
        var bookingItemDto = new BookingItemDtoOut();
        bookingItemDto.setId(booking.getId());
        bookingItemDto.setStart(booking.getStart());
        bookingItemDto.setEnd(booking.getEnd());
        bookingItemDto.setBookerId(booking.getBooker().getId());
        return bookingItemDto;
    }


    public static List<BookingDtoOut> toBookingDtos(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    public static Booking toBooking(BookingDtoIn bookingDto, ItemRepository itemRepository) {
        var booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        Long itemId = bookingDto.getItemId();
        booking.setItem(itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundByIdException(itemId)));
        return booking;
    }
}

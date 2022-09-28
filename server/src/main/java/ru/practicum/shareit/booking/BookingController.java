package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getBookingsByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String bookingState,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getBookingsByBookerId(userId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getBookingsByItemsOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") String bookingState,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        return bookingService.getBookingsByItemOwnerId(userId, bookingState, from, size);
    }

    @PostMapping
    public BookingDtoOut saveBooking(@Valid @RequestBody BookingDtoIn bookingDtoIn,
                                     @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return bookingService.saveBooking(bookingDtoIn, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long bookingId,
                                             @RequestParam boolean approved) {
        return bookingService.updateBookingStatus(bookingId, userId, approved);
    }
}

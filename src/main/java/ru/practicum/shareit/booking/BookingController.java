package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable Long bookingId) {
        return bookingService.getBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDtoOut> getAllBookingsByBookerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                        @RequestParam(
                                                                name = "state",
                                                                defaultValue = "ALL") String bookingState) {
        return bookingService.getAllBookingsByBookerId(userId, bookingState);
    }

    @GetMapping("/owner")
    public List<BookingDtoOut> getAllBookingsByItemsOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                            @RequestParam(
                                                                    name = "state",
                                                                    defaultValue = "ALL") String bookingState) {
        return bookingService.getAllBookingsByItemsOwnerId(userId, bookingState);
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

package ru.practicum.shareit.client.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.client.booking.dto.BookingDtoIn;
import ru.practicum.shareit.client.booking.dto.BookingState;
import ru.practicum.shareit.client.booking.exception.UnknownBookingStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                          @PathVariable Long bookingId) {
        return bookingClient.getBookingById(bookerId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByBookerId(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(name = "state", defaultValue = "ALL") String bookingState,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        BookingState.from(bookingState).orElseThrow(() -> new UnknownBookingStateException(bookingState));
        return bookingClient.getBookingsByBookerId(bookerId, bookingState, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllByItemsOwnerId(
            @RequestHeader("X-Sharer-User-Id") Long bookerId,
            @RequestParam(name = "state", defaultValue = "ALL") String bookingState,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        BookingState.from(bookingState).orElseThrow(() -> new UnknownBookingStateException(bookingState));
        return bookingClient.getBookingsByItemOwnerId(bookerId, bookingState, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                         @Valid @RequestBody BookingDtoIn bookingDtoIn) {
        return bookingClient.createBooking(bookerId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                      @PathVariable Long bookingId,
                                                      @RequestParam boolean approved) {
        return bookingClient.updateBookingStatus(bookerId, bookingId, approved);
    }
}

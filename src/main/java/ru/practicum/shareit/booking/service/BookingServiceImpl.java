package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.booking.BookingAccessException;
import ru.practicum.shareit.exception.booking.BookingException;
import ru.practicum.shareit.exception.booking.BookingNotFoundByIdException;
import ru.practicum.shareit.exception.booking.UnknownBookingStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserService userService;

    @Override
    public BookingDtoOut getBooking(Long bookerId, Long bookingId) {
        var booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new BookingNotFoundByIdException(bookingId));

        if (!(booking.getBooker().getId().equals(bookerId) || booking.getItem().getOwner().getId().equals(bookerId))) {
            throw new BookingAccessException(
                    String.format("The user {id=%s} is not allowed to view the item {id=%s}", bookerId, bookingId));
        }

        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByBookerId(Long bookerId, String bookingState) {
        BookingState state;
        try {
            state = BookingState.valueOf(bookingState);
        } catch (IllegalArgumentException ignore) {
            throw new UnknownBookingStateException(bookingState);
        }

        userService.getUserById(bookerId);

        List<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findCurrentBookingsByBookerId(bookerId);
                break;
            case PAST:
                bookings = bookingRepository.findPastBookingsByBookerId(bookerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureBookingsByBookerId(bookerId);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatus(bookerId, BookingStatus.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findAllByBookerId(bookerId);
                break;
            default:
                bookings = Collections.emptyList();
        }
        return BookingMapper.toBookingDtos(bookings);
    }

    @Override
    public List<BookingDtoOut> getAllBookingsByItemsOwnerId(Long itemsOwnerId, String bookingState) {
        BookingState state;
        try {
            state = BookingState.valueOf(bookingState);
        } catch (IllegalArgumentException ignore) {
            throw new UnknownBookingStateException(bookingState);
        }

        userService.getUserById(itemsOwnerId);

        List<Booking> bookings;
        switch (state) {
            case CURRENT:
                bookings = bookingRepository.findCurrentBookingsByItemOwnerId(itemsOwnerId);
                break;
            case PAST:
                bookings = bookingRepository.findPastBookingsByItemOwnerId(itemsOwnerId);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemsOwnerId);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(itemsOwnerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatus(itemsOwnerId, BookingStatus.REJECTED);
                break;
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerId(itemsOwnerId);
                break;
            default:
                bookings = Collections.emptyList();
        }
        return BookingMapper.toBookingDtos(bookings);
    }

    @Override
    public BookingDtoOut saveBooking(BookingDtoIn bookingDtoIn, Long bookerId) {
        var booker = userService.getUserById(bookerId);
        var booking = BookingMapper.toBooking(bookingDtoIn, itemRepository);
        validateBooking(booking, bookerId);

        booking.setStatus(BookingStatus.WAITING);
        booking.setBooker(UserMapper.toUser(booker));

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut updateBookingStatus(Long bookingId, Long userId, boolean approved) {
        userService.getUserById(userId);
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundByIdException(bookingId));

        if (!userId.equals(booking.getItem().getOwner().getId())) {
            throw new BookingAccessException("You must be the owner of the item to approve or reject the booking");
        }

        var status = booking.getStatus();
        if (BookingStatus.APPROVED.equals(status) || BookingStatus.REJECTED.equals(status)) {
            throw new BookingException(String.format("The booking {id=%s} has %s status", bookingId, status));
        }

        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    private void validateBooking(Booking booking, Long bookerId) {
        Item item = booking.getItem();
        Long itemId = item.getId();
        var now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (!item.getAvailable()) {
            throw new BookingException(String.format("The booking item {id=%s} is unavailable", itemId));
        }

        Long ownerId = item.getOwner().getId();
        if (bookerId.equals(ownerId)) {
            throw new BookingAccessException(
                    String.format("The owner {id=%s} cannot book his own item {id=%s}", itemId, ownerId));
        }

        var start = booking.getStart();
        if (start.isBefore(now)) {
            throw new BookingException(
                    String.format("The booking start time (%s) cannot be later than the current moment (%s)",
                            start.format(formatter), now.format(formatter)));
        }

        var end = booking.getEnd();
        if (start.isAfter(end)) {
            throw new BookingException(
                    String.format("The booking start (%s) time cannot be later than the booking end time (%s)",
                            start.format(formatter), end.format(formatter)));
        }
    }
}

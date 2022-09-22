package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.booking.BookingAccessException;
import ru.practicum.shareit.exception.booking.BookingException;
import ru.practicum.shareit.exception.booking.BookingNotFoundByIdException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private User owner;
    private Item item;
    private Booking booking;

    private BookingDtoIn bookingDtoIn;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final LocalDateTime NOW = LocalDateTime.parse(LocalDateTime.now().format(DATE_TIME_FORMATTER));

    private static final long USER_ID = 1L;
    private static final long BOOKER_ID = 2;
    private static final long ITEM_ID = 1;
    private static final long BOOKING_ID = 1;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);
        user.setName("UserName");
        user.setEmail("UserEmail");

        owner = new User();
        owner.setId(BOOKER_ID);
        owner.setName("BookerName");
        owner.setEmail("BookerEmail");

        item = new Item();
        item.setId(ITEM_ID);
        item.setName("ItemName");
        item.setDescription("ItemDescription");
        item.setAvailable(true);
        item.setOwner(user);

        booking = new Booking();
        booking.setId(BOOKING_ID);
        booking.setItem(item);
        booking.setBooker(owner);
        booking.setStatus(BookingStatus.WAITING);
        booking.setStart(NOW);
        booking.setEnd(NOW.plusDays(2L));

        bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setStart(NOW.plusMinutes(5));
        bookingDtoIn.setEnd(NOW.plusMinutes(10));
        bookingDtoIn.setItemId(ITEM_ID);
    }

    @ParameterizedTest
    @ValueSource(longs = {USER_ID, BOOKER_ID})
    void getBookingById(long userId) {
        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));

        bookingService.getBooking(userId, BOOKING_ID);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(BOOKING_ID);
    }

    @Test
    void getBookingByWrongUserId() {
        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));

        long wrongUserId = 3; // not booker id AND not item owner id
        var bookingException = Assertions.assertThrows(BookingException.class,
                () -> bookingService.getBooking(wrongUserId, BOOKING_ID));

        Assertions.assertEquals(String.format("The user {id=%s} is not allowed to view the item {id=%s}",
                        wrongUserId, ITEM_ID),
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(BOOKING_ID);
    }

    @Test
    void getBookingByWrongBookingId() {
        long wrongBookingId = 100;
        Mockito.when(bookingRepository.findById(wrongBookingId))
                .thenThrow(new BookingNotFoundByIdException(wrongBookingId));


        var bookingException = Assertions.assertThrows(BookingNotFoundByIdException.class,
                () -> bookingService.getBooking(USER_ID, wrongBookingId));

        Assertions.assertEquals(String.format("Booking with {id=%s} not found", wrongBookingId),
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(wrongBookingId);
    }

    @Test
    void getBookingsByBookerId() {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));

        Mockito.when(bookingRepository.findAllByBookerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findAllByBookerIdAndStatus(eq(USER_ID), eq(BookingStatus.REJECTED),
                        Mockito.any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(bookingRepository.findCurrentBookingsByBookerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findFutureBookingsByBookerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findPastBookingsByBookerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));

        final int from = 0;
        final int size = 10;

        List<BookingDtoOut> bookings;

        bookings = bookingService.getBookingsByBookerId(USER_ID, BookingState.ALL.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByBookerId(USER_ID, BookingState.REJECTED.name(), from, size);
        assertThat(bookings.size(), is(0));

        bookings = bookingService.getBookingsByBookerId(USER_ID, BookingState.CURRENT.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByBookerId(USER_ID, BookingState.FUTURE.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByBookerId(USER_ID, BookingState.PAST.name(), from, size);
        assertThat(bookings.size(), is(1));

        Mockito.verify(userService, Mockito.times(5))
                .getUserById(USER_ID);
    }

    @Test
    void getBookingsByItemOwnerId() {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));

        Mockito.when(bookingRepository.findAllByItemOwnerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findAllByItemOwnerIdAndStatus(eq(USER_ID), eq(BookingStatus.REJECTED),
                        Mockito.any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        Mockito.when(bookingRepository.findCurrentBookingsByItemOwnerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findFutureBookingsByItemOwnerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        Mockito.when(bookingRepository.findPastBookingsByItemOwnerId(eq(USER_ID), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));

        final int from = 0;
        final int size = 10;

        List<BookingDtoOut> bookings;

        bookings = bookingService.getBookingsByItemsOwnerId(USER_ID, BookingState.ALL.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByItemsOwnerId(USER_ID, BookingState.REJECTED.name(), from, size);
        assertThat(bookings.size(), is(0));

        bookings = bookingService.getBookingsByItemsOwnerId(USER_ID, BookingState.CURRENT.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByItemsOwnerId(USER_ID, BookingState.FUTURE.name(), from, size);
        assertThat(bookings.size(), is(1));

        bookings = bookingService.getBookingsByItemsOwnerId(USER_ID, BookingState.PAST.name(), from, size);
        assertThat(bookings.size(), is(1));

        Mockito.verify(userService, Mockito.times(5))
                .getUserById(USER_ID);
    }

    @Test
    void saveBooking() {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenReturn(booking);

        bookingService.saveBooking(bookingDtoIn, BOOKER_ID);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void saveBookingWithUnavailableItem() {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));

        item.setAvailable(false);
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(item));

        final var bookingException = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.saveBooking(bookingDtoIn, BOOKER_ID));

        Assertions.assertEquals(String.format("The booking item {id=%s} is unavailable", ITEM_ID),
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void saveBookingByItemOwner() {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));

        item.setOwner(owner);
        Mockito.when(itemRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(item));

        final var bookingException = Assertions.assertThrows(
                BookingAccessException.class,
                () -> bookingService.saveBooking(bookingDtoIn, BOOKER_ID));

        Assertions.assertEquals(String.format("The owner {id=%s} cannot book his own item {id=%s}",
                        ITEM_ID, owner.getId()), bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.never())
                .save(Mockito.any());
    }

    private static Stream<Arguments> updateBookingStatusProvider() {
        return Stream.of(
                Arguments.of(true, BookingStatus.APPROVED),
                Arguments.of(false, BookingStatus.REJECTED)
        );
    }

    @ParameterizedTest
    @MethodSource("updateBookingStatusProvider")
    void updateBookingStatus(boolean approved, BookingStatus bookingStatus) {
        Mockito.when(userService.getUserById(Mockito.any()))
                .thenReturn(UserMapper.toUserDto(user));

        Mockito.when(bookingRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(booking));

        Mockito.when(bookingRepository.save(Mockito.any()))
                .thenAnswer(invocationOnMock -> {
                    if (approved) {
                        booking.setStatus(BookingStatus.APPROVED);
                    } else {
                        booking.setStatus(BookingStatus.REJECTED);
                    }
                    return booking;
                });


        var bookingDto = bookingService.updateBookingStatus(BOOKING_ID, USER_ID, approved);
        assertThat(bookingStatus, is(bookingDto.getStatus()));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(Mockito.any());
    }

    @Test
    void updateBookingStatusByWrongBookingId() {
        final long wrongBookingId = 100;

        final var bookingException = Assertions.assertThrows(
                BookingNotFoundByIdException.class,
                () -> bookingService.updateBookingStatus(wrongBookingId, USER_ID, true));

        Assertions.assertEquals(String.format("Booking with {id=%s} not found", wrongBookingId),
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.never())
                .save(Mockito.any());
    }

    @Test
    void updateBookingStatusByNotOwnerId() {
        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));

        final long notOwnerId = 5;

        final var bookingException = Assertions.assertThrows(
                BookingAccessException.class,
                () -> bookingService.updateBookingStatus(BOOKING_ID, notOwnerId, true));

        Assertions.assertEquals("You must be the owner of the item to approve or reject the booking",
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.never())
                .save(Mockito.any());
    }


    @ParameterizedTest
    @ValueSource(strings = {"APPROVED", "REJECTED"})
    void updateBookingStatusByNotOwnerId(String bookingStatus) {
        Mockito.when(bookingRepository.findById(BOOKING_ID))
                .thenReturn(Optional.of(booking));

        booking.setStatus(BookingStatus.valueOf(bookingStatus));

        final var bookingException = Assertions.assertThrows(
                BookingException.class,
                () -> bookingService.updateBookingStatus(BOOKING_ID, USER_ID, true));

        Assertions.assertEquals(String.format("The booking {id=%s} has %s status", BOOKING_ID, bookingStatus),
                bookingException.getMessage());

        Mockito.verify(bookingRepository, Mockito.never())
                .save(Mockito.any());
    }
}

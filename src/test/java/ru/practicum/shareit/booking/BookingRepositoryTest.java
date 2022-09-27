package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private User userDB;
    private Item item;
    private Item itemDB;

    @BeforeEach
    void init() {
        user = new User();
        user.setName("UserName");
        user.setEmail("userBD@mail.com");

        item = new Item();
        item.setName("itemBD");
        item.setDescription("itemBD");
        item.setAvailable(true);

        userDB = tem.persist(user);
        itemDB = tem.persist(item);

        item.setOwner(userDB);
    }

    @Test
    void findCurrentBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByBookerId(userDB.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByBookerId(userDB.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByBookerIdWithRepositoryFilledWithOnlyFutureBookings() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByBookerId(userDB.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByBookerId(userDB.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findFutureBookingsByBookerIdWithRepositoryFilledWithOnlyPastBookings() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        var bookings = bookingRepository.findFutureBookingsByBookerId(userDB.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    private static Stream<Arguments> findAllByBookerIdAndStatusProvider() {
        return Stream.of(
                Arguments.of(BookingStatus.APPROVED, 1),
                Arguments.of(BookingStatus.WAITING, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("findAllByBookerIdAndStatusProvider")
    void findAllByBookerIdAndStatus(BookingStatus bookingStatus, int bookingCount) {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByBookerIdAndStatus(userDB.getId(), bookingStatus,
                Pageable.unpaged());
        assertThat(bookings.size(), is(bookingCount));
    }

    @Test
    void findCurrentBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByItemOwnerId(itemDB.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByItemOwnerId(itemDB.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByItemOwnerIdWithRepositoryFilledWithOnlyFutureBookings() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        var bookings = bookingRepository.findPastBookingsByItemOwnerId(itemDB.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemDB.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findFutureBookingsByItemOwnerIdWithRepositoryFilledWithOnlyPastBookings() {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemDB.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    private static Stream<Arguments> findAllByItemOwnerIdAndStatusProvider() {
        return Stream.of(
                Arguments.of(BookingStatus.APPROVED, 1),
                Arguments.of(BookingStatus.WAITING, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("findAllByItemOwnerIdAndStatusProvider")
    void findAllByItemOwnerIdAndStatus(BookingStatus bookingStatus, int bookingCount) {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByItemOwnerIdAndStatus(itemDB.getOwner().getId(),
                bookingStatus, Pageable.unpaged());
        assertThat(bookings.size(), is(bookingCount));
    }

    private static Stream<Arguments> findLastBookingByItemIdProvider() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), true),
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), false)
        );
    }

    @ParameterizedTest
    @MethodSource("findLastBookingByItemIdProvider")
    void findLastBookingByItemId(LocalDateTime start, LocalDateTime end, boolean exist) {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(start);
        booking.setEnd(end);

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastBookingByItemId(itemDB.getId());
        assertThat(maybeBooking.isPresent(), is(exist));
    }

    private static Stream<Arguments> findNextBookingByItemIdProvider() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), true),
                Arguments.of(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), false)
        );
    }

    @ParameterizedTest
    @MethodSource("findNextBookingByItemIdProvider")
    void findNextBookingByItemId(LocalDateTime start, LocalDateTime end, boolean exist) {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(start);
        booking.setEnd(end);

        tem.persist(booking);

        var maybeBooking = bookingRepository.findNextBookingByItemId(itemDB.getId());
        assertThat(maybeBooking.isPresent(), is(exist));
    }

    private static Stream<Arguments> findLastFinishedBookingByBookerIdAndItemIdProvider() {
        return Stream.of(
                Arguments.of(LocalDateTime.now().minusDays(2), LocalDateTime.now().minusDays(1), true),
                Arguments.of(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2), false)
        );
    }

    @ParameterizedTest
    @MethodSource("findLastFinishedBookingByBookerIdAndItemIdProvider")
    void findLastFinishedBookingByBookerIdAndItemId(LocalDateTime start, LocalDateTime end, boolean exist) {
        var booking = new Booking();
        booking.setBooker(userDB);
        booking.setItem(itemDB);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(start);
        booking.setEnd(end);

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastFinishedBookingByBookerIdAndItemId(
                userDB.getId(), itemDB.getId());
        assertThat(maybeBooking.isPresent(), is(exist));
    }
}

package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private BookingRepository bookingRepository;

    private User user;
    private User user2;
    private Item item;

    @BeforeEach
    void init() {
        user = new User();
        user.setName("UserName");
        user.setEmail("user@mail.com");

        user2 = new User();
        user2.setName("name2");
        user2.setEmail("email2@mail.ru");

        item = new Item();
        item.setName("item");
        item.setDescription("item");
        item.setAvailable(true);
        item.setOwner(user);

        tem.persist(user);
        tem.persist(user2);
        tem.persist(item);
        tem.flush();
    }

    @Test
    void findCurrentBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByBookerId(user.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByBookerId(user.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookings = bookingRepository.findPastBookingsByBookerId(user.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByBookerId(user.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookings = bookingRepository.findFutureBookingsByBookerId(user.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findAllByBookerIdAndStatus() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByBookerIdAndStatus(user.getId(), BookingStatus.APPROVED,
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        bookings = bookingRepository.findAllByBookerIdAndStatus(user.getId(), BookingStatus.WAITING,
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findCurrentBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByItemOwnerId(item.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByItemOwnerId(item.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookings = bookingRepository.findPastBookingsByItemOwnerId(item.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByItemOwnerId(item.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookings = bookingRepository.findFutureBookingsByItemOwnerId(item.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByItemOwnerIdAndStatus(item.getOwner().getId(),
                BookingStatus.APPROVED, Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        bookings = bookingRepository.findAllByItemOwnerIdAndStatus(item.getOwner().getId(),
                BookingStatus.WAITING, Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findLastBookingByItemId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastBookingByItemId(item.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        maybeBooking = bookingRepository.findLastBookingByItemId(item.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }

    @Test
    void findNextBookingByItemId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findNextBookingByItemId(item.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        maybeBooking = bookingRepository.findNextBookingByItemId(item.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }

    @Test
    void findLastFinishedBookingByBookerIdAndItemId() {
        var booking = new Booking();
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastFinishedBookingByBookerIdAndItemId(
                user.getId(), item.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        maybeBooking = bookingRepository.findLastFinishedBookingByBookerIdAndItemId(user.getId(), item.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }
}

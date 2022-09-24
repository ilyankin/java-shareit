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
    private User userBD;
    private Item item;
    private Item itemBD;

    @BeforeEach
    void init() {
        userBD = new User();
        userBD.setName("UserName");
        userBD.setEmail("userBD@mail.com");

        itemBD = new Item();
        itemBD.setName("itemBD");
        itemBD.setDescription("itemBD");
        itemBD.setAvailable(true);
        itemBD.setOwner(userBD);

        userBD = tem.persist(userBD);
        itemBD = tem.persist(itemBD);
    }

    @Test
    void findCurrentBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByBookerId(userBD.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByBookerId(userBD.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookings = bookingRepository.findPastBookingsByBookerId(userBD.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByBookerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByBookerId(userBD.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookings = bookingRepository.findFutureBookingsByBookerId(userBD.getId(), Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findAllByBookerIdAndStatus() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByBookerIdAndStatus(userBD.getId(), BookingStatus.APPROVED,
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        bookings = bookingRepository.findAllByBookerIdAndStatus(userBD.getId(), BookingStatus.WAITING,
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findCurrentBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusSeconds(1));

        tem.persist(booking);

        var bookings = bookingRepository.findCurrentBookingsByItemOwnerId(itemBD.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findPastBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var bookings = bookingRepository.findPastBookingsByItemOwnerId(itemBD.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        bookings = bookingRepository.findPastBookingsByItemOwnerId(itemBD.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findFutureBookingsByItemOwnerId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemBD.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        bookings = bookingRepository.findFutureBookingsByItemOwnerId(itemBD.getOwner().getId(),
                Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findAllByItemOwnerIdAndStatus() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now());

        tem.persist(booking);

        var bookings = bookingRepository.findAllByItemOwnerIdAndStatus(itemBD.getOwner().getId(),
                BookingStatus.APPROVED, Pageable.unpaged());
        assertThat(bookings.size(), is(1));

        bookings = bookingRepository.findAllByItemOwnerIdAndStatus(itemBD.getOwner().getId(),
                BookingStatus.WAITING, Pageable.unpaged());
        assertThat(bookings.size(), is(0));
    }

    @Test
    void findLastBookingByItemId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastBookingByItemId(itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        maybeBooking = bookingRepository.findLastBookingByItemId(itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }

    @Test
    void findNextBookingByItemId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findNextBookingByItemId(itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        maybeBooking = bookingRepository.findNextBookingByItemId(itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }

    @Test
    void findLastFinishedBookingByBookerIdAndItemId() {
        var booking = new Booking();
        booking.setBooker(userBD);
        booking.setItem(itemBD);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setStart(LocalDateTime.now().minusDays(2));
        booking.setEnd(LocalDateTime.now().minusDays(1));

        tem.persist(booking);

        var maybeBooking = bookingRepository.findLastFinishedBookingByBookerIdAndItemId(
                userBD.getId(), itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(true));

        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        maybeBooking = bookingRepository.findLastFinishedBookingByBookerIdAndItemId(userBD.getId(), itemBD.getId());
        assertThat(maybeBooking.isPresent(), is(false));
    }
}

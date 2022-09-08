package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BookingRepository extends JpaRepository<Booking, Long>, BookingRepositoryCustom {

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "order by b.start desc")
    List<Booking> findAllByBookerId(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start <= current_timestamp " +
            "and b.end > current_timestamp " +
            "")
    List<Booking> findCurrentBookingsByBookerId(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc")
    List<Booking> findPastBookingsByBookerId(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc")
    List<Booking> findFutureBookingsByBookerId(Long bookerId);

    @Query("select b from Booking b " +
            "where b.booker.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc")
    List<Booking> findAllByBookerIdAndStatus(Long bookerId, BookingStatus bookingStatus);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerId(Long itemOwnerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.start <= current_timestamp " +
            "and b.end > current_timestamp " +
            "order by b.start desc ")
    List<Booking> findCurrentBookingsByItemOwnerId(Long itemsOwnerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.end < current_timestamp " +
            "order by b.start desc ")
    List<Booking> findPastBookingsByItemOwnerId(Long itemsOwnerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.start > current_timestamp " +
            "order by b.start desc ")
    List<Booking> findFutureBookingsByItemOwnerId(Long itemsOwnerId);

    @Query("select b from Booking b " +
            "where b.item.owner.id = ?1 " +
            "and b.status = ?2 " +
            "order by b.start desc ")
    List<Booking> findAllByItemOwnerIdAndStatus(Long itemsOwnerId, BookingStatus bookingStatus);
}

package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Optional;

@Transactional
public interface BookingRepositoryCustom {
    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "and b.end_date < now() " +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findLastBookingByItemId(Long itemId);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 " +
            "and b.start_date > now() " +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findNextBookingByItemId(Long itemId);

    @Query(value = "select * from bookings b " +
            "where b.booker_id = ?1 " +
            "and b.item_id = ?2 " +
            "and b.end_date < now() " +
            "order by b.start_date desc " +
            "limit 1", nativeQuery = true)
    Optional<Booking> findLastFinishedBookingByBookerIdAndItemId(Long bookerId, Long itemId);
}

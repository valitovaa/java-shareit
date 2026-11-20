package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerId(Long userId);

    List<Booking> findAllByBookerIdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(
            Long userId, LocalDateTime end, LocalDateTime start);

    List<Booking> findAllByBookerIdAndEndBefore(
            Long userId, LocalDateTime now);

    List<Booking> findAllByBookerIdAndStartAfter(
            Long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStatus(Long userId, BookingStatus status);

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfter(
            Long userId, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItem_Owner_IdAndEndBefore(Long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_IdAndStartAfter(Long userId, LocalDateTime now);

    List<Booking> findAllByItem_Owner_Id(Long userId);

    List<Booking> findAllByItem_IdInAndStatusAndEndBeforeOrderByStartDesc(
            List<Long> itemIds,
            BookingStatus status,
            LocalDateTime now
    );

    boolean existsByBookerIdAndItem_IdAndStatusAndEndBefore(Long userId, Long itemId, BookingStatus bookingStatus, LocalDateTime now);

    List<Booking> findAllByItem_IdInAndStatusAndStartAfterOrderByStartAsc(List<Long> itemIds, BookingStatus bookingStatus, LocalDateTime now);
}


package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Component
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByBookerIdAndStatusOrderByStartDesc(
            Long userId, BookingStatus status
    );

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long userId, LocalDateTime start, LocalDateTime end
    );

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(
            Long userId, LocalDateTime now
    );

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(
            Long userId, LocalDateTime now
    );

    List<Booking> findAllByItem_Owner_IdAndStatusOrderByStartDesc(
            Long userId, BookingStatus status
    );

    List<Booking> findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(
            Long userId, LocalDateTime start, LocalDateTime end
    );

    List<Booking> findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(
            Long userId, LocalDateTime now
    );

    List<Booking> findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(
            Long userId, LocalDateTime now
    );

    List<Booking> findAllByItem_Owner_IdOrderByStartDesc(Long userId);

    List<Booking> findAllByItem_IdInAndStatusAndEndBeforeOrderByStartDesc(
            List<Long> itemIds,
            BookingStatus status,
            LocalDateTime now
    );

    boolean existsByBookerIdAndItem_IdAndStatusAndEndBefore(
            Long userId,
            Long itemId,
            BookingStatus bookingStatus,
            LocalDateTime now
    );

    List<Booking> findAllByItem_IdInAndStatusAndStartAfterOrderByStartAsc(
            List<Long> itemIds,
            BookingStatus bookingStatus,
            LocalDateTime now
    );
}

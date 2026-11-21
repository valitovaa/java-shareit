
package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BookingStatusException;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImp implements BookingService {

    private final BookingRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    @Override
    public Booking createBooking(Booking booking, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена"));

        User booker = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        booking.setItem(item);
        booking.setBooker(booker);

        LocalDateTime now = LocalDateTime.now();

        if (!item.getAvailable()) {
            throw new ConditionsNotMetException("Вещь недоступна для бронирования");
        }

        if (item.getOwner().getId().equals(booker.getId())) {
            throw new ConditionsNotMetException("Нельзя забронировать собственную вещь");
        }

        if (!booking.getStart().isAfter(now)) {
            throw new ConditionsNotMetException("Дата начала бронирования должна быть в будущем");
        }

        if (!booking.getEnd().isAfter(booking.getStart())) {
            throw new ConditionsNotMetException("Дата окончания должна быть позже даты начала");
        }

        booking.setStatus(BookingStatus.WAITING);

        return repository.save(booking);
    }


    @Transactional
    @Override
    public Booking bookingApproval(Long bookingId, Long userId, Boolean approved) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может изменить статус бронирования");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new BookingStatusException("Статус бронирования уже изменён");
        }

        if (Boolean.TRUE.equals(approved)) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return repository.save(booking);
    }

    @Override
    public Booking getBookingInfo(Long bookingId, Long userId) {
        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        boolean isOwner = booking.getItem().getOwner().getId().equals(userId);
        boolean isBooker = booking.getBooker().getId().equals(userId);

        if (!isOwner && !isBooker) {
            throw new ForbiddenException("Нет прав на просмотр информации о бронировании");
        }

        return booking;
    }

    @Override
    public List<Booking> getBookings(Long userId, BookingState state) {
        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case ALL -> repository.findAllByBookerIdOrderByStartDesc(userId);
            case WAITING -> repository.findAllByBookerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.WAITING);
            case REJECTED -> repository.findAllByBookerIdAndStatusOrderByStartDesc(
                    userId, BookingStatus.REJECTED);
            case CURRENT -> repository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, now, now);
            case PAST -> repository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                    userId, now);
            case FUTURE -> repository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                    userId, now);
        };
    }

    @Override
    public List<Booking> getBookingsByOwner(Long userId, BookingState state) {

        LocalDateTime now = LocalDateTime.now();

        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }

        return switch (state) {
            case ALL -> repository.findAllByItem_Owner_IdOrderByStartDesc(userId);
            case WAITING -> repository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(
                    userId, BookingStatus.WAITING);
            case REJECTED -> repository.findAllByItem_Owner_IdAndStatusOrderByStartDesc(
                    userId, BookingStatus.REJECTED);
            case CURRENT -> repository.findAllByItem_Owner_IdAndStartBeforeAndEndAfterOrderByStartDesc(
                    userId, now, now);
            case PAST -> repository.findAllByItem_Owner_IdAndEndBeforeOrderByStartDesc(
                    userId, now);
            case FUTURE -> repository.findAllByItem_Owner_IdAndStartAfterOrderByStartDesc(
                    userId, now);
        };
    }
}

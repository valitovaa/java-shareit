package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImp implements BookingService {
    BookingRepository repository;

    @Override
    public Booking createBooking(Booking booking) {
        booking.setStatus(BookingStatus.WAITING);
        return repository.save(booking);
    }

    @Override
    public Booking bookingApproval(Long bookingId, Long userId, Boolean approved) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Только владелец вещи может изменить статус бронирования");
        }

        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new IllegalStateException("Статус бронирования уже изменён");
        }

        if (approved == true) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return booking;
    }

    @Override
    public Booking getBookingInfo(Long bookingId, Long userId) {

        Booking booking = repository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Бронирование не найдено"));

        if (!booking.getItem().getOwner().getId().equals(userId)
                && !booking.getBooker().getId().equals(userId)) {
            throw new ForbiddenException("Нет прав на просмотр информации о бронировании");
        }

        return booking;
    }

    @Override
    public List<Booking> getBookings(Long userId, BookingState state) {

        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case ALL -> repository.findAllByBookerId(userId);

            case WAITING -> repository.findAllByBookerIdAndStatus(
                    userId, BookingStatus.WAITING);

            case REJECTED -> repository.findAllByBookerIdAndStatus(
                    userId, BookingStatus.REJECTED);

            case CURRENT -> repository.findAllByBookerIdAndStartBeforeAndEndAfter(
                    userId, now, now);

            case PAST -> repository.findAllByBookerIdAndEndBefore(
                    userId, now);

            case FUTURE -> repository.findAllByBookerIdAndStartAfter(
                    userId, now);
        };

    }

    public List<Booking> getBookingsByOwner(Long userId, BookingState state) {

        LocalDateTime now = LocalDateTime.now();

        return switch (state) {
            case ALL -> repository.findAllByItem_Owner_Id(userId);

            case WAITING -> repository.findAllByItem_Owner_IdAndStatus(
                    userId, BookingStatus.WAITING);

            case REJECTED -> repository.findAllByItem_Owner_IdAndStatus(
                    userId, BookingStatus.REJECTED);

            case CURRENT -> repository.findAllByItem_Owner_IdAndStartBeforeAndEndAfter(
                    userId, now, now);

            case PAST -> repository.findAllByItem_Owner_IdAndEndBefore(
                    userId, now);

            case FUTURE -> repository.findAllByItem_Owner_IdAndStartAfter(
                    userId, now);
        };
    }

}

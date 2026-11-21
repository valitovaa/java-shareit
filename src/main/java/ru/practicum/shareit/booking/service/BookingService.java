package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {

    Booking createBooking(Booking booking, Long itemId, Long userId);

    Booking bookingApproval(Long bookingId, Long userId, Boolean approved);

    Booking getBookingInfo(Long bookingId, Long userId);

    List<Booking> getBookings(Long userId, BookingState state);

    List<Booking> getBookingsByOwner(Long userId, BookingState state);
}

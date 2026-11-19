package ru.practicum.shareit.booking.service;

import jakarta.validation.Valid;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;

import java.util.List;

public interface BookingService {


    Booking createBooking(@Valid Booking booking);

    Booking bookingApproval(@Valid Long bookingId, Long userId, Boolean approved);

    Booking getBookingInfo(Long bookingId, Long userId);

    List<Booking> getBookings(Long userId, BookingState state);

    List<Booking> getBookingsByOwner(Long userId, BookingState state);
}

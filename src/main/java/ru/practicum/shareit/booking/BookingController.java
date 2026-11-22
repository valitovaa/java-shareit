package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dal.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(
            BookingService bookingService
    ) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public BookingDto createBooking(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody BookingRequestDto bookingDto) {

        Booking booking = BookingMapper.map(bookingDto);

        Booking res = bookingService.createBooking(booking, bookingDto.getItemId(), userId);

        return BookingMapper.map(res);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto bookingApproval(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam Boolean approved
    ) {

        Booking res = bookingService.bookingApproval(bookingId, userId, approved);

        return BookingMapper.map(res);

    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingInfo(
            @PathVariable Long bookingId,
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {

        Booking res = bookingService.getBookingInfo(bookingId, userId);

        return BookingMapper.map(res);
    }

    @GetMapping
    public List<BookingDto> getUserBookingsByState(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") BookingState state
    ) {
        List<Booking> res = bookingService.getBookings(userId, state);
        return convertListToDto(res);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestParam(defaultValue = "ALL") BookingState state,
            @RequestHeader("X-Sharer-User-Id") Long userId) {

        List<Booking> res = bookingService.getBookingsByOwner(userId, state);

        return convertListToDto(res);

    }

    public List<BookingDto> convertListToDto(List<Booking> bookingList) {
        return bookingList.stream()
                .map(BookingMapper::map)
                .toList();
    }

}

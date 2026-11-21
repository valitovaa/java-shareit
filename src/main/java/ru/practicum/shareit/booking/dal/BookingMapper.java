package ru.practicum.shareit.booking.dal;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;

public class BookingMapper {

    public static BookingDto map(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus().toString(),
                new BookingBookerDto(booking.getBooker().getId()),
                new BookingItemDto(
                        booking.getItem().getId(),
                        booking.getItem().getName()
                )
        );
    }

    public static Booking map(BookingRequestDto bookingDto) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        return booking;
    }

    public static BookingInfoDto mapToInfo(Booking booking) {
        if (booking == null) {
            return null;
        }

        return new BookingInfoDto(
                booking.getId(),
                booking.getBooker().getId()
        );
    }


}

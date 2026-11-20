package ru.practicum.shareit.booking.dal;

import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

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

    public static Booking map(
            BookingRequestDto bookingDto,
            Item item,
            User booker
    ) {

        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);

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

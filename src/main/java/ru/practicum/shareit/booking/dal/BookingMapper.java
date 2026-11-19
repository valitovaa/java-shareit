package ru.practicum.shareit.booking.dal;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {

    public static BookingDto map(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus().toString()
        );
    }

    public static Booking map(
            BookingDto bookingDto,
            Item item,
            User booker
    ) {
        Booking booking = new Booking();

        booking.setId(bookingDto.getId());
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

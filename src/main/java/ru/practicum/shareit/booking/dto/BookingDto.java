package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class BookingDto {

    Long id;

    @NotNull(message = "начало бронирования должно быть указано")
    LocalDateTime start;

    @NotNull(message = "конец бронирования должен быть указан")
    LocalDateTime end;
    @NotNull
    Long itemId;
    @NotNull
    Long bookerId;
    String status;

}

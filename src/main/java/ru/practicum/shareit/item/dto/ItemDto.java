package ru.practicum.shareit.item.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {

    private Long id;
    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;

    @NotNull(message = "Доступность вещи должна быть указана")
    private Boolean available;

    private Long requestId;

    private List<CommentDto> comments;

    private BookingInfoDto lastBooking;

    private BookingInfoDto nextBooking;

}

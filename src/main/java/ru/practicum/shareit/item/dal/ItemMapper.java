package ru.practicum.shareit.item.dal;

import ru.practicum.shareit.booking.dto.BookingInfoDto;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public class ItemMapper {

    public static ItemDto map(
            Item item,
            List<CommentDto> comments,
            BookingInfoDto lastBooking,
            BookingInfoDto nextBooking
    ) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                comments,
                lastBooking,
                nextBooking
        );
    }

    public static Item map(ItemDto itemDto) {
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }
}
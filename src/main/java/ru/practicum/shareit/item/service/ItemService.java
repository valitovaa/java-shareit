package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public interface ItemService {

    Item addItem(Item item);

    Item findItemById(Long id);

    Item updateItem(Long itemId, ItemDto itemDto, Long userId);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> searchItems(String text);
}

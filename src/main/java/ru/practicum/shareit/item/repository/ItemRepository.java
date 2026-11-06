package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    Item addItem(Item item);

    Optional<Item> findItemById(Long itemId);

    void updateItem(Item item);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> searchItems(String text);
}

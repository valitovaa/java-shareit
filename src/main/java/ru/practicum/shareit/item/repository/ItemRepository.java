package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Component
public interface ItemRepository {
    Item addItem(Item item);

    Optional<Item> findItemById(Long itemId);

    void updateItem(Item item);

    List<Item> findAllByOwnerId(Long userId);

    List<Item> searchItems(String text);
}

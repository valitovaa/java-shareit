package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryItemRepository implements ItemRepository{

    private final Map<Long, Item> items = new HashMap<>();
    private long currentId = 0;


    @Override
    public Item addItem(Item item) {

        item.setId(getNextId());
        items.put(item.getId(),item);

        return item;
    }

    @Override
    public Optional<Item> findItemById(Long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public void updateItem(Item item) {
        items.put(item.getId(),item);
    }

    @Override
    public List<Item> findAllByOwnerId(Long userId) {
        return  items.values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .toList();
    }

    @Override
    public List<Item> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String searchText = text.toLowerCase();

        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(searchText)
                                || item.getDescription().toLowerCase().contains(searchText))
                .toList();
    }


    private long getNextId() {
        return ++currentId;
    }

}

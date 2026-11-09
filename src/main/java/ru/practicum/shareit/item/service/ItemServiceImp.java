package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {

    public final ItemRepository itemRepository;

    @Override
    public Item addItem(Item item) {
        return itemRepository.addItem(item);
    }

    @Override
    public Item findItemById(Long id) {
        return itemRepository.findItemById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }


    @Override
    public Item updateItem(Long itemId, ItemDto itemDto, Long userId) {

        Item item = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет не найден"));

        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Нет прав на редактирование");
        }

        if (itemDto.getName() != null) {
            if (itemDto.getName().isBlank()) {
                throw new ConditionsNotMetException("Название вещи не может быть пустым");
            }
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            if (itemDto.getDescription().isBlank()) {
                throw new ConditionsNotMetException("Описание вещи не может быть пустым");
            }
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        itemRepository.updateItem(item);

        return item;
    }

    @Override
    public List<Item> findAllByOwnerId(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public List<Item> searchItems(String text) {
        return itemRepository.searchItems(text);
    }

}
